package com.eden.orchid.api.resources.resourceSource;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.resources.resource.JarResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.Provider;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarResourceSource implements OrchidResourceSource {

    private final Provider<OrchidContext> context;

    @Getter
    private final Class<?> pluginClass;

    @Getter
    private final int priority;

    public JarResourceSource(Provider<OrchidContext> context, int priority) {
        this.context = context;
        this.pluginClass = this.getClass();
        this.priority = priority;
    }

    public JarResourceSource(Provider<OrchidContext> context, Class<? extends OrchidModule> moduleClass, int priority) {
        this.context = context;
        this.pluginClass = moduleClass;
        this.priority = priority;
    }

    private JarFile jarForClass() {
        String path = "/" + pluginClass.getName().replace('.', '/') + ".class";
        URL jarUrl = pluginClass.getResource(path);
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
        JarFile jarFile = jarForClass();

        if (jarFile != null) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                if (entry.getName().endsWith(fileName) && !entry.isDirectory()) {
                    return new JarResource(context.get(), jarFile, entry);
                }
            }
        }

        return null;
    }

    @Override
    public List<OrchidResource> getResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        List<OrchidResource> entries = new ArrayList<>();

        JarFile jarFile = jarForClass();

        if (jarFile == null) {
            return entries;
        }

        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            // we are checking a file in the jar
            if (OrchidUtils.normalizePath(jarEntry.getName()).startsWith(path + "/") && !jarEntry.isDirectory()) {

                if (EdenUtils.isEmpty(fileExtensions) || FilenameUtils.isExtension(jarEntry.getName(), fileExtensions)) {

                    if (shouldAddEntry(jarEntry.getName())) {
                        entries.add(new JarResource(context.get(), jarFile, jarEntry));
                    }
                }
            }
        }

        return entries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JarResourceSource)) return false;
        JarResourceSource that = (JarResourceSource) o;
        return getPriority() == that.getPriority() && Objects.equals(pluginClass, that.pluginClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pluginClass, getPriority());
    }

    @Override
    public int compareTo(@NotNull OrchidResourceSource o) {
        if(o instanceof JarResourceSource) {
            int superValue = o.getPriority() - getPriority();

            if(superValue != 0) {
                return superValue;
            }
            return ((JarResourceSource) o).getPluginClass().getName().compareTo(getPluginClass().getName());
        }
        else {
            return o.getPriority() - getPriority();
        }
    }
}
