package com.eden.orchid.resources;

import com.eden.orchid.Orchid;
import com.eden.orchid.utilities.JSONElement;
import com.eden.orchid.utilities.OrchidUtils;

import java.io.File;
import java.util.Arrays;

public abstract class OrchidResource {

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

        String[] namePieces = name.split(File.separator);
        filePath = String.join(File.separator, Arrays.copyOfRange(namePieces, 0, namePieces.length - 1));
        fileName = namePieces[namePieces.length - 1];
    }

    public String getContent() {
        return content;
    }
    public String getRawContent() {
        return rawContent;
    }

    public JSONElement getEmbeddedData() {
        return embeddedData;
    }

    public JSONElement queryEmbeddedData(String pointer) {
        if(embeddedData != null) {
            return embeddedData.query(pointer);
        }

        return null;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

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
