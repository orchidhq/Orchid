package com.eden.orchid.api.resources.resourceSource;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.JarResource;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class DefaultResourceSource extends OrchidResourceSource {

    private JarFile jarForClass(Class<?> clazz) {
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
    public OrchidResource getResourceEntry(String fileName) {
        JarFile jarFile = jarForClass(this.getClass());

        if (jarFile != null) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                if (entry.getName().endsWith(fileName) && !entry.isDirectory()) {
                    return new JarResource(jarFile, entry);
                }
            }
        }

        return null;
    }

    @Override
    public List<OrchidResource> getResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        List<OrchidResource> entries = new ArrayList<>();

        JarFile jarFile = jarForClass(this.getClass());

        if (jarFile == null) {
            return entries;
        }

        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            // we are checking a file in the jar
            if (jarEntry.getName().startsWith(path + File.separator) && !jarEntry.isDirectory()) {

                if (EdenUtils.isEmpty(fileExtensions) || FilenameUtils.isExtension(jarEntry.getName(), fileExtensions)) {

                    if (shouldAddEntry(jarEntry.getName())) {
                        entries.add(new JarResource(jarFile, jarEntry));
                    }
                }
            }
        }

        return entries;
    }
}
