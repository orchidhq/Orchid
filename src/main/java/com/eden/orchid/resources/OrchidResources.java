package com.eden.orchid.resources;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.OrchidUtils;
import com.sun.tools.javac.resources.compiler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class OrchidResources {

    public static Map<Integer, ResourceSource> resourceSources = new TreeMap<>(Collections.reverseOrder());

    public static void registerResouceSource(ResourceSource resourceSource) {
        int priority = resourceSource.resourcePriority();
        while(resourceSources.containsKey(priority)) {
            priority--;
        }

        OrchidResources.resourceSources.put(priority, resourceSource);
    }

    /**
     * Returns the jar file used to load class clazz, or null if clazz was not loaded from a jar.
     *
     * @param clazz  the class to load a jar from
     * @return  the JarFile for a given class, or null if the class was not loaded from a jar
     */
    public static JarFile jarForClass(Class<?> clazz) {
        String path = "/" + clazz.getName().replace('.', '/') + ".class";
        URL jarUrl = clazz.getResource(path);
        if (jarUrl == null) {
            return null;
        }

        String url = jarUrl.toString();
        int bang = url.indexOf("!");
        String JAR_URI_PREFIX = "jar:file:";
        if (url.startsWith(JAR_URI_PREFIX) && bang != -1) {
            try {
                return new JarFile(url.substring(JAR_URI_PREFIX.length(), bang));
            }
            catch (IOException e) {
                throw new IllegalStateException("Error loading jar file.", e);
            }
        }
        else {
            return null;
        }
    }


    /**
     * Gets an OrchidEntry representing the file found in the '-resourcesDir' option directory. Typically used when you
     * need a file that you know will be provided by the end-user.
     *
     * @param fileName the name of the requested file, relative to the 'resources' directory
     * @return  the OrchidEntry representing the file if it exists in the resourcesDir location, null otherwise
     */
    public static OrchidEntry getResourceDirEntry(String fileName) {
        File file = getResourceFile(fileName);

        if(file != null) {
            return new OrchidEntry(file, fileName);
        }

        return null;
    }

    /**
     * Gets an OrchidEntry representing the file found in one of many possible locations. This will return the first file
     * found in the following order: if the '-resourcesDir' option was set, the File from that directory, otherwise the
     * File from the default resources directory. If the requested file could not be found in the resource directories,
     * it will loop through all registered ResourceSources in order of priority from highest to lowest and attempt to
     * find a JarEntry matching the same fileName. Typically used to get any file that is not expected to be provided by
     * the end-user.
     *
     * @param fileName the name of the requested file, relative to the 'resources' directory
     * @return  the OrchidEntry representing the file if it exists in any location, null otherwise
     */
    public static OrchidEntry getResourceEntry(String fileName) {
        File file = getResourceFile(fileName);

        if(file != null) {
            return new OrchidEntry(file, fileName);
        }

        for(Map.Entry<Integer, ResourceSource> source : OrchidResources.resourceSources.entrySet()) {
            JarFile jarFile = jarForClass(source.getValue().getClass());
            JarEntry jarEntry = getJarFile(jarFile, fileName);
            if(jarEntry != null) {
                return new OrchidEntry(jarFile, jarEntry, fileName);
            }
        }

        return null;
    }

    /**
     * Gets a list of OrchidEntries representing the files found in the '-resourcesDir' option directory. Typically used
     * when you need files that you know will be provided by the end-user.
     *
     * @param dirName the name of requested directory, relative to the 'resources' directory
     * @return the entries, in no particular order
     */
    public static List<OrchidEntry> getResourceDirEntries(String dirName, String[] fileExtensions, boolean recursive) {
        TreeMap<String, OrchidEntry> entries = new TreeMap<>();

        List<File> files = getResourceFiles(dirName, fileExtensions, recursive);

        for(File file : files) {
            String relative = getRelativeFilename(file.getAbsolutePath(), dirName);

            OrchidEntry entry = new OrchidEntry(file, relative);
            entry.setPriority(Integer.MAX_VALUE);
            entries.put(relative, entry);
        }

        return new ArrayList<>(entries.values());
    }

    /**
     * Gets a list of OrchidEntries representing the file found in one of many possible locations. This will return all
     * files from the following locations: if the '-resourcesDir' option was set, the File from that directory, otherwise
     * the File from the default resources directory. Also, it will loop through all registered ResourceSources in order
     * of priority from highest to lowest and find all matching JarEntries. Entries in the resulting list are ordered such
     * that files toward the end of the list have the highest priortiy, so iterating across all entries and writing the
     * appripriate files will naturally allow the resources from higher-priority ResourceSources or overwrite
     * lower-priority ones, with local resources from the '-resourcesDir' option taking ultimate precedence.
     *
     * @param dirName the name of requested directory, relative to the 'resources' directory
     * @return the entries, ordered by ResourceSource priority
     */
    public static List<OrchidEntry> getResourceEntries(String dirName, String[] fileExtensions, boolean recursive) {
        TreeMap<String, OrchidEntry> entries = new TreeMap<>();

        List<File> files = getResourceFiles(dirName, fileExtensions, recursive);
        Clog.d("Getting resources from resourcesDir");
        for(File file : files) {
            String relative = getRelativeFilename(file.getAbsolutePath(), dirName);

            OrchidEntry entry = new OrchidEntry(file, relative);
            entry.setPriority(Integer.MAX_VALUE);
            entries.put(relative, entry);
        }

        for(Map.Entry<Integer, ResourceSource> source : OrchidResources.resourceSources.entrySet()) {
            Clog.d("Getting resources from jar: #{$1 | className}", source.getValue());

            JarFile jarFile = jarForClass(source.getValue().getClass());

            List<JarEntry> jarEntries = getJarResourceFiles(jarFile, dirName, fileExtensions, recursive);

            for(JarEntry jarEntry : jarEntries) {
                String relative = getRelativeFilename(jarEntry.getName(), dirName);

                boolean shouldAddJarEntry = false;
                if(entries.containsKey(relative)) {
                    if(source.getValue().resourcePriority() > entries.get(relative).getPriority()) {
                        shouldAddJarEntry = true;
                    }
                }
                else {
                    shouldAddJarEntry = true;
                }

                if(shouldAddJarEntry) {
                    OrchidEntry entry = new OrchidEntry(jarFile, jarEntry, relative);
                    entries.put(relative, entry);
                }
            }
        }

        return new ArrayList<>(entries.values());
    }

    private static String getJarEntryFileName(JarEntry jarEntry) {
        String[] namePieces = jarEntry.getName().split(File.separator);

        return namePieces[namePieces.length - 1];
    }

    private static File getResourceFile(String fileName) {
        File resourceFile = null;

        fileName = fileName.replaceAll("/", File.separator);

        // if we've specified a resources dir, use that
        if(!OrchidUtils.isEmpty(Orchid.query("options.resourcesDir"))) {
            File file = new File(Orchid.query("options.resourcesDir") + File.separator + fileName);

            if(file.exists() && !file.isDirectory()) {
                resourceFile = file;
            }
        }

        // otherwise, use the project default resources directory
        else {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            URL fileUrl = classloader.getResource(fileName);

            if(fileUrl != null) {
                try {
                    URI fileUri = fileUrl.toURI();

                    File file = new File(fileUri);

                    if(file.exists() && !file.isDirectory()) {
                        resourceFile = file;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // return the file content if it was found, otherwise return null
        return resourceFile;
    }

    private static JarEntry getJarFile(JarFile jarfile, String fileName) {
        Enumeration<JarEntry> entries = jarfile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();

            if (entry.getName().endsWith(fileName) && !entry.isDirectory()) {
                return entry;
            }
        }

        return null;
    }

    /**
     * Get the resource Files that are in the given path and match the given file extensions. If the -resourcesDir
     * option is set, search in that directory, otherwise search in the default project resources directory.
     *
     * @param path  the relative path to find files in
     * @param fileExtensions  the file extensions to filter files by
     * @param recursive  whether to recursively search all subdirectories in the given path
     * @return  the list of matching Files
     */
    private static List<File> getResourceFiles(String path, String[] fileExtensions, boolean recursive) {
        ArrayList<File> files = new ArrayList<>();

        // if we've specified a resources dir, use that
        if(!OrchidUtils.isEmpty(Orchid.query("options.resourcesDir"))) {
            File file = new File(Orchid.query("options.resourcesDir") + File.separator + path);

            if(file.exists() && file.isDirectory()) {
                files.addAll(new ArrayList<File>(FileUtils.listFiles(file, fileExtensions, recursive)));
            }
        }

        // otherwise, use the project default resources directory
        else {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            URL fileUrl = classloader.getResource(path);

            if(fileUrl != null) {
                try {
                    URI fileUri = fileUrl.toURI();

                    File file = new File(fileUri);

                    if(file.exists() && file.isDirectory()) {
                        files.addAll(new ArrayList<File>(FileUtils.listFiles(file, fileExtensions, recursive)));
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return files;
    }

    /**
     * Get the entries from a Jar that are in the given path and match the given file extensions.
     *
     * @param jarfile  the JarFile to search for resources
     * @param path  the path to search in the Jar
     * @param fileExtensions  (optional) the list of extensions to match the entries
     * @return  the list of matching JarEntries
     */
    private static List<JarEntry> getJarResourceFiles(JarFile jarfile, String path, String[] fileExtensions, boolean recursive) {
        ArrayList<JarEntry> files = new ArrayList<>();

        Enumeration<JarEntry> entries = jarfile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            // we are checking a file in the jar
            if (entry.getName().startsWith(path + File.separator) && !entry.isDirectory()) {

                if(OrchidUtils.isEmpty(fileExtensions) || FilenameUtils.isExtension(entry.getName(), fileExtensions)) {
                    files.add(entry);
                }
            }
        }

        return files;
    }

    public static void writeFile(String outputDir, String fileName, String contents) {
        String outputPath = Orchid.query("options.d").getElement().toString() + File.separator + outputDir.replaceAll("/", File.separator);

        File outputFile = new File(outputPath);
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }

        try {
            Path classesFile = Paths.get(outputPath + File.separator + fileName);
            Files.write(classesFile, contents.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getRelativeFilename(String sourcePath, String baseDir) {
        if(sourcePath.contains(baseDir)) {
            int indexOf = sourcePath.indexOf(baseDir);

            if(indexOf + baseDir.length() < sourcePath.length()) {
                String relative = sourcePath.substring((indexOf + baseDir.length()));

                if(relative.startsWith(File.separator)) {
                    relative = relative.substring(1);
                }

                return relative;
            }
        }

        return sourcePath;
    }
}
