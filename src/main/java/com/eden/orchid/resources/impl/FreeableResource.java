package com.eden.orchid.resources.impl;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.Orchid;
import com.eden.orchid.resources.OrchidReference;
import com.eden.orchid.resources.OrchidResource;

public abstract class FreeableResource extends OrchidResource {

    public FreeableResource(OrchidReference reference) {
        super(reference);
    }

    protected void loadContent() {
        if(rawContent != null) {
            EdenPair<String, JSONElement> parsedContent = Orchid.getTheme().getEmbeddedData(rawContent);
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

        return super.getContent();
    }

    @Override
    public String getRawContent() {
        loadContent();

        return super.getRawContent();
    }

    @Override
    public JSONElement getEmbeddedData() {
        loadContent();

        return super.getEmbeddedData();
    }

    @Override
    public JSONElement queryEmbeddedData(String pointer) {
        loadContent();

        return super.queryEmbeddedData(pointer);
    }
}
