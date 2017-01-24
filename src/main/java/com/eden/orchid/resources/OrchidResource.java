package com.eden.orchid.resources;

import com.eden.common.json.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.utilities.OrchidUtils;

import java.io.File;

public abstract class OrchidResource {

    protected OrchidReference reference;

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

        this.reference = new OrchidReference(name);
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

    public OrchidReference getReference() {
        return reference;
    }

    public void setReference(OrchidReference reference) {
        this.reference = reference;
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
