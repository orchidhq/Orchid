package com.eden.orchid.resources.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.resources.OrchidResource;
import com.eden.orchid.utilities.JSONElement;
import com.eden.orchid.utilities.OrchidPair;
import com.eden.orchid.utilities.OrchidUtils;

import java.io.File;
import java.util.Arrays;

public abstract class FreeableResource implements OrchidResource {

    protected String fileName;
    protected String filePath;

    protected String rawContent;
    protected String content;
    protected JSONElement embeddedData;

    protected int priority;

    public void setName(String name) {

        String baseDir;

        if(!OrchidUtils.isEmpty(Orchid.query("options.resourcesDir"))) {
            baseDir = Orchid.query("options.resourcesDir").toString();
        }
        else {
            baseDir = "";
        }

        if(name.contains(baseDir)) {
            int indexOf = name.indexOf(baseDir);

            if(indexOf + baseDir.length() < name.length()) {
                String relative = name.substring((indexOf + baseDir.length()));

                if(relative.startsWith(File.separator)) {
                    relative = relative.substring(1);
                }

                name = relative;
            }
        }

        Clog.v("baseDir: #{$1} - #{$2}", new Object[]{baseDir, name});

        String[] namePieces = name.split(File.separator);
        filePath = String.join(File.separator, Arrays.copyOfRange(namePieces, 0, namePieces.length - 1));
        fileName = namePieces[namePieces.length - 1];
    }

    protected void loadContent() {
        if(rawContent != null) {
            OrchidPair<String, JSONElement> parsedContent = Orchid.getTheme().getEmbeddedData(rawContent);
            content = parsedContent.first;
            embeddedData = parsedContent.second;
        }
        else {
            rawContent = "";
            content = "";
            embeddedData = null;
        }
    }

    public void free() {
        rawContent = null;
        content = null;
    }

    @Override
    public String getContent() {
        loadContent();

        return content;
    }

    @Override
    public String getRawContent() {
        loadContent();

        return rawContent;
    }

    @Override
    public JSONElement getEmbeddedData() {
        loadContent();

        return embeddedData;
    }

    @Override
    public JSONElement queryEmbeddedData(String pointer) {
        loadContent();

        if(embeddedData != null) {
            return embeddedData.query(pointer);
        }

        return null;
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

    public void setEmbeddedData(JSONElement embeddedData) {
        this.embeddedData = embeddedData;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
