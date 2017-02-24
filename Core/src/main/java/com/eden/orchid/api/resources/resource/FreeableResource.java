package com.eden.orchid.api.resources.resource;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.resources.OrchidReference;

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
            EdenPair<String, JSONElement> parsedContent = reference.getContext().getTheme().getEmbeddedData(rawContent);
            this.content = parsedContent.first;
            this.embeddedData = parsedContent.second;

            this.shouldPrecompile = (this.embeddedData != null);
        }
        else {
            this.rawContent = "";
            this.content = "";
            this.embeddedData = null;

            this.shouldPrecompile = false;
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

    @Override
    public boolean shouldPrecompile() {
        loadContent();

        return super.shouldPrecompile();
    }
}
