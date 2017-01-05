package com.eden.orchid.utilities.resources;

import com.eden.orchid.utilities.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.utilities.OrchidPair;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class OrchidEntry {

    private final File file;

    private final JarEntry jarEntry;
    private final JarFile jarFile;

    private String path;
    private String fileName;

    private String rawContent;
    private String precompiledContent;
    private JSONElement embeddedData;

    private int priority;

    public OrchidEntry(File file, String name) {
        this.file = file;

        jarEntry = null;
        jarFile = null;

        setName(name);
    }

    public OrchidEntry(JarFile jarFile, JarEntry jarEntry, String name) {
        this.file = null;

        this.jarEntry = jarEntry;
        this.jarFile = jarFile;

        setName(name);
    }

    public String getRawContent() {
        loadContent();

        return rawContent;
    }

    public String getContent() {
        loadContent();

        return precompiledContent;
    }

    public JSONElement getEmbeddedData() {
        loadContent();

        return embeddedData;
    }

    public void setEmbeddedData(JSONElement embeddedData) {
        this.embeddedData = embeddedData;
    }

    public JSONElement queryEmbeddedData(String pointer) {
        loadContent();

        if(embeddedData != null) {
            return embeddedData.query(pointer);
        }

        return null;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setName(String name) {
        String[] namePieces = name.split(File.separator);
        path = String.join(File.separator, Arrays.copyOfRange(namePieces, 0, namePieces.length - 1));
        fileName = namePieces[namePieces.length - 1];
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public String getName() {
        return path + File.separator + fileName;
    }

    // internal methods to get the data represented by this Entry
//----------------------------------------------------------------------------------------------------------------------
    private void loadContent() {
        if(rawContent == null) {
            try {
                if (file != null) {
                    rawContent = IOUtils.toString(new FileInputStream(file));
                }
                else if(jarFile != null && jarEntry != null) {
                    rawContent = IOUtils.toString(jarFile.getInputStream(jarFile.getEntry(jarEntry.getName())));
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            if(rawContent != null) {
                OrchidPair<String, JSONElement> parsedContent = Orchid.getTheme().getEmbeddedData(rawContent);
                precompiledContent = parsedContent.first;
                embeddedData = parsedContent.second;
            }
            else {
                rawContent = "";
                precompiledContent = "";
                embeddedData = null;
            }
        }
    }
}
