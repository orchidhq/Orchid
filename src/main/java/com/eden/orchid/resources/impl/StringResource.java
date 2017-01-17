package com.eden.orchid.resources.impl;

import com.eden.orchid.Orchid;
import com.eden.orchid.resources.OrchidResource;
import com.eden.orchid.utilities.JSONElement;
import com.eden.orchid.utilities.OrchidPair;

import java.io.File;
import java.util.Arrays;

public final class StringResource implements OrchidResource {

    protected String fileName;
    protected String filePath;

    protected final String rawContent;
    protected final String content;
    protected JSONElement embeddedData;

    protected int priority;

    public StringResource(String name, String content) {
        String[] namePieces = name.split(File.separator);
        this.filePath = String.join(File.separator, Arrays.copyOfRange(namePieces, 0, namePieces.length - 1));
        this.fileName = namePieces[namePieces.length - 1];

        if(content != null) {
            OrchidPair<String, JSONElement> parsedContent = Orchid.getTheme().getEmbeddedData(content);
            this.rawContent = content;
            this.content = parsedContent.first;
            this.embeddedData = parsedContent.second;
        }
        else {
            this.rawContent = "";
            this.content = "";
            this.embeddedData = null;
        }
    }

    @Override
    public JSONElement queryEmbeddedData(String pointer) {
        if(embeddedData != null) {
            return embeddedData.query(pointer);
        }

        return null;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getRawContent() {
        return rawContent;
    }

    @Override
    public JSONElement getEmbeddedData() {
        return embeddedData;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
