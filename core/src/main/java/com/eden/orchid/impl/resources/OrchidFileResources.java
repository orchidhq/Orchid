package com.eden.orchid.impl.resources;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.resources.OrchidReference;
import com.eden.orchid.api.resources.OrchidResource;
import com.eden.orchid.api.resources.OrchidResourceSource;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.utilities.AlwaysSortedTreeSet;
import com.eden.orchid.utilities.OrchidUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class OrchidFileResources implements OrchidResources, OrchidResourceSource {

    private Set<OrchidResourceSource> resourceSources;

    private int resourcePriority = 1;

    /**
     * Returns the jar file used to load class clazz, or null if clazz was not loaded from a jar.
     *
     * @param clazz  the class to load a jar from
     * @return  the JarFile for a given class, or null if the class was not loaded from a jar
     */
    public JarFile jarForClass(Class<?> clazz) {
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

    @Override
    public OrchidResource getResourceDirEntry(String fileName) {
        File file = getResourceFile(fileName);

        if(file != null) {
            return new FileResource(file);
        }

        return null;
    }

    @Override
    public OrchidResource getResourceEntry(String fileName) {
        File file = getResourceFile(fileName);

        if(file != null) {
            return new FileResource(file);
        }

        if(resourceSources == null) {
            resourceSources = new AlwaysSortedTreeSet<>(OrchidUtils.resolveSet(OrchidResourceSource.class));
        }

        for(OrchidResourceSource source : resourceSources) {
            if(source.getResourcePriority() < 0) { continue; }

            JarFile jarFile = jarForClass(source.getClass());
            JarEntry jarEntry = getJarFile(jarFile, fileName);
            if (jarEntry != null) {
                return new JarResource(jarFile, jarEntry);
            }
        }

        return null;
    }

    @Override
    public List<OrchidResource> getResourceDirEntries(String dirName, String[] fileExtensions, boolean recursive) {
        TreeMap<String, OrchidResource> entries = new TreeMap<>();

        List<File> files = getResourceFiles(dirName, fileExtensions, recursive);

        for(File file : files) {
            String relative = getRelativeFilename(file.getAbsolutePath(), dirName);

            FileResource entry = new FileResource(file);
            entry.setPriority(Integer.MAX_VALUE);
            entries.put(relative, entry);
        }

        return new ArrayList<>(entries.values());
    }

    @Override
    public List<OrchidResource> getResourceEntries(String dirName, String[] fileExtensions, boolean recursive) {
        TreeMap<String, OrchidResource> entries = new TreeMap<>();

        List<File> files = getResourceFiles(dirName, fileExtensions, recursive);
        for(File file : files) {
            String relative = getRelativeFilename(file.getAbsolutePath(), dirName);

            FileResource entry = new FileResource(file);
            entry.setPriority(Integer.MAX_VALUE);
            entries.put(relative, entry);
        }

        if(resourceSources == null) {
            resourceSources = new AlwaysSortedTreeSet<>(OrchidUtils.resolveSet(OrchidResourceSource.class));
        }

        for(OrchidResourceSource source : resourceSources) {
            if(source.getResourcePriority() < 0) { continue; }

            JarFile jarFile = jarForClass(source.getClass());

            List<JarEntry> jarEntries = getJarResourceFiles(jarFile, dirName, fileExtensions, recursive);

            for(JarEntry jarEntry : jarEntries) {
                String relative = getRelativeFilename(jarEntry.getName(), dirName);

                boolean shouldAddJarEntry = false;
                if(entries.containsKey(relative)) {
                    if(source.getResourcePriority() > entries.get(relative).getPriority()) {
                        shouldAddJarEntry = true;
                    }
                }
                else {
                    shouldAddJarEntry = true;
                }

                if(shouldAddJarEntry) {
                    JarResource entry = new JarResource(jarFile, jarEntry);
                    entries.put(relative, entry);
                }
            }
        }

        return new ArrayList<>(entries.values());
    }

    public String getJarEntryFileName(JarEntry jarEntry) {
        String[] namePieces = jarEntry.getName().split(File.separator);

        return namePieces[namePieces.length - 1];
    }

    public File getResourceFile(String fileName) {
        File resourceFile = null;

        fileName = fileName.replaceAll("/", File.separator);

        // if we've specified a resources dir, use that
        if(!EdenUtils.isEmpty(Orchid.getContext().query("options.resourcesDir"))) {
            File file = new File(Orchid.getContext().query("options.resourcesDir") + File.separator + fileName);

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

    public JarEntry getJarFile(JarFile jarfile, String fileName) {
        if(jarfile != null) {
            Enumeration<JarEntry> entries = jarfile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                if (entry.getName().endsWith(fileName) && !entry.isDirectory()) {
                    return entry;
                }
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
        if(!EdenUtils.isEmpty(Orchid.getContext().query("options.resourcesDir"))) {
            String fullPath = Orchid.getContext().query("options.resourcesDir") + File.separator + path;
            File file = new File(fullPath);

            if(file.exists() && file.isDirectory()) {
                Collection newFiles = FileUtils.listFiles(file, fileExtensions, recursive);
                files.addAll(newFiles);
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
    public List<JarEntry> getJarResourceFiles(JarFile jarfile, String path, String[] fileExtensions, boolean recursive) {
        ArrayList<JarEntry> files = new ArrayList<>();

        if(jarfile == null) {
            return files;
        }

        Enumeration<JarEntry> entries = jarfile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            // we are checking a file in the jar
            if (entry.getName().startsWith(path + File.separator) && !entry.isDirectory()) {

                if(EdenUtils.isEmpty(fileExtensions) || FilenameUtils.isExtension(entry.getName(), fileExtensions)) {
                    files.add(entry);
                }
            }
        }

        return files;
    }

    public String getRelativeFilename(String sourcePath, String baseDir) {
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

    public OrchidResource getProjectReadme() {
        if(!EdenUtils.isEmpty(Orchid.getContext().query("options.resourcesDir"))) {
            String resourceDir = Orchid.getContext().query("options.resourcesDir").toString();

            File folder = new File(resourceDir);

            // set hard limit of searching no more than 10 parent directories for the README
            int maxIterations = 10;

            while(true) {
                if(folder.isDirectory()) {
                    List<File> files = new ArrayList<>(FileUtils.listFiles(folder, null, false));

                    for (File file : files) {
                        if(FilenameUtils.removeExtension(file.getName()).equalsIgnoreCase("readme")) {
                            Clog.i("Found README file: #{$1}", new Object[]{file.getAbsolutePath()});
                            return new FileResource(file);
                        }
                    }
                }

                // set the folder to its own parent and search again
                if(folder.getParentFile() != null && maxIterations > 0) {
                    folder = folder.getParentFile();
                    maxIterations--;
                }

                // there is no more parent to search, exit the loop
                else {
                    break;
                }
            }
        }

        return null;
    }

    public OrchidResource getProjectLicense() {
        if(!EdenUtils.isEmpty(Orchid.getContext().query("options.resourcesDir"))) {
            String resourceDir = Orchid.getContext().query("options.resourcesDir").toString();

            File folder = new File(resourceDir);

            // set hard limit of searching no more than 10 parent directories for the README
            int maxIterations = 10;

            while(true) {
                if(folder.isDirectory()) {
                    List<File> files = new ArrayList<>(FileUtils.listFiles(folder, null, false));

                    for (File file : files) {
                        if(FilenameUtils.removeExtension(file.getName()).equalsIgnoreCase("license")) {
                            Clog.i("Found License file: #{$1}", new Object[]{file.getAbsolutePath()});

                            return new FileResource(file);
                        }
                    }
                }

                // set the folder to its own parent and search again
                if(folder.getParentFile() != null && maxIterations > 0) {
                    folder = folder.getParentFile();
                    maxIterations--;
                }

                // there is no more parent to search, exit the loop
                else {
                    break;
                }
            }
        }

        return null;
    }

    public boolean render(
            String template,
            String extension,
            boolean templateReference,
            JSONObject pageData,
            String alias,
            OrchidReference reference) {
        String templateContent = "";
        if(templateReference) {
            OrchidResource templateResource = getResourceEntry(template);

            if(templateResource == null) {
                templateResource = getResourceEntry("templates/pages/index.twig");
            }
            if(templateResource == null) {
                templateResource = getResourceEntry("templates/pages/index.html");
            }
            if(templateResource == null) {
                return false;
            }

            templateContent = templateResource.getContent();
        }
        else {
            templateContent = (EdenUtils.isEmpty(template)) ? "" : template;
        }

        JSONObject templateVariables = new JSONObject(getContext().getRoot().toMap());
        templateVariables.put("page", pageData);
        if(!EdenUtils.isEmpty(alias)) {
            templateVariables.put(alias, pageData);
        }

        String content = getContext().getTheme().compile(extension, templateContent, templateVariables);

        String outputPath = reference.getFullPath();
        String outputName = reference.getFileName() + "." + reference.getOutputExtension();

        outputPath = Orchid.getContext().query("options.d").getElement().toString() + File.separator + outputPath.replaceAll("/", File.separator);

        File outputFile = new File(outputPath);
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }

        try {
            Path classesFile = Paths.get(outputPath + File.separator + outputName);
            Files.write(classesFile, content.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    @Override
    public int getResourcePriority() {
        return resourcePriority;
    }

    @Override
    public void setResourcePriority(int priority) {
        this.resourcePriority = priority;
    }
}
