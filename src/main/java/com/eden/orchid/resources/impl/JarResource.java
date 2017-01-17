package com.eden.orchid.resources.impl;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class JarResource extends FreeableResource {

    private final JarEntry jarEntry;
    private final JarFile jarFile;

    public JarResource(JarFile jarFile, JarEntry jarEntry) {
        this.jarEntry = jarEntry;
        this.jarFile = jarFile;

        setName(jarEntry.getName());
    }

    @Override
    protected void loadContent() {
        if(rawContent == null) {
            try {
                if(jarFile != null && jarEntry != null) {
                    rawContent = IOUtils.toString(jarFile.getInputStream(jarFile.getEntry(jarEntry.getName())));
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.loadContent();
    }
}
