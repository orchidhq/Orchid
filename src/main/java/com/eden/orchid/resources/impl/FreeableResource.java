package com.eden.orchid.resources.impl;

import com.eden.orchid.Orchid;
import com.eden.orchid.resources.OrchidResource;
import com.eden.orchid.utilities.JSONElement;
import com.eden.orchid.utilities.OrchidPair;

public abstract class FreeableResource extends OrchidResource {

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
