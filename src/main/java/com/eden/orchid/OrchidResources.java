package com.eden.orchid;

import com.eden.orchid.utilities.OrchidPair;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class OrchidResources {

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

    public static File getResourceFile(String fileName) {
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

    public static JarEntry getJarFile(JarFile jarfile, String fileName) {
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
    public List<File> getResourceFiles(String path, String[] fileExtensions, boolean recursive) {
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
    public List<JarEntry> getJarResourceFiles(JarFile jarfile, String path, String[] fileExtensions) {
        ArrayList<JarEntry> files = new ArrayList<>();

        Enumeration<JarEntry> entries = jarfile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();

            // we are checking a file in the jar
            if (entry.getName().startsWith(path + File.separator)
                    && !entry.isDirectory()
                    && FilenameUtils.isExtension(entry.getName(), fileExtensions)) {
                files.add(entry);
            }
        }

        return files;
    }

    public static void writeFile(String outputDir, String fileName, String contents) {
        String outputPath = Orchid.query("options.d").getElement().toString()
                + File.separator + outputDir.replaceAll("/", File.separator);

        File outputFile = new File(outputPath);
        Path classesFile = Paths.get(outputPath + File.separator + fileName);
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }

        try {
            Files.write(classesFile, contents.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFileContents(JarFile jarFile, JarEntry jarEntry) {
        try {
            InputStream is = jarFile.getInputStream(jarFile.getEntry(jarEntry.getName()));

            return IOUtils.toString(is);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getFileContents(File file) {
        if(file != null) {
            try {
                return IOUtils.toString(new FileInputStream(file));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static OrchidPair<String, JSONElement> readResource(String fileName) {
        String content;

        if(fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }

        content = OrchidResources.getFileContents(OrchidResources.getResourceFile(fileName));

        if(OrchidUtils.isEmpty(content)) {
            JarFile jar = jarForClass(Orchid.getTheme().getClass());
            content = OrchidResources.getFileContents(jar, OrchidResources.getJarFile(jar, fileName));
        }

        if(OrchidUtils.isEmpty(content)) {
            JarFile jar = jarForClass(Orchid.class);
            content = OrchidResources.getFileContents(jar, OrchidResources.getJarFile(jar, fileName));
        }

        if(content != null) {
            return Orchid.getTheme().getEmbeddedData(content);
        }

        return new OrchidPair<String, JSONElement>("", null);
    }
}
