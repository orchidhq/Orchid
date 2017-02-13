package com.eden.orchid.impl.resources;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.resources.OrchidReference;
import com.eden.orchid.api.resources.OrchidResource;

/**
 * An abstract concept of a 'freeable' resource, that is one that is able to look its contents back up, and so can be
 * safely 'freed' when requested to save memory. Such an example would be a file, because having a reference to the
 * file makes it possible to get rid of the actual contents when needed because the file can be read back into memory
 * when the data is needed.
 */
public abstract class FreeableResource extends OrchidResource {

    public FreeableResource(OrchidReference reference) {
        super(reference);
    }

    protected void loadContent() {
        if(rawContent != null) {
            EdenPair<String, JSONElement> parsedContent = getTheme().getEmbeddedData(rawContent);
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
