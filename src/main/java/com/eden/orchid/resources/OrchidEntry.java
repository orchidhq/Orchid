package com.eden.orchid.resources;

import com.eden.orchid.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.utilities.OrchidPair;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class OrchidEntry {

    private final File file;

    private final JarEntry jarEntry;
    private final JarFile jarFile;

    private String sourcePath;
    private String destPath;
    private String outputName;

    private String rawContent;
    private String precompiledContent;
    private JSONElement embeddedData;

    public OrchidEntry(File file, String fileName) {
        this.file = file;

        jarEntry = null;
        jarFile = null;
    }

    public OrchidEntry(JarFile jarFile, JarEntry jarEntry, String fileName) {
        this.file = null;

        this.jarEntry = jarEntry;
        this.jarFile = jarFile;
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

    public JSONElement queryEmbeddedData(String pointer) {
        loadContent();

        if(embeddedData != null) {
            return embeddedData.query(pointer);
        }

        return null;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public String getDestPath() {
        return destPath;
    }

    public String getOutputName() {
        return outputName;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public void setOutputName(String outputName) {
        this.outputName = outputName;
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
