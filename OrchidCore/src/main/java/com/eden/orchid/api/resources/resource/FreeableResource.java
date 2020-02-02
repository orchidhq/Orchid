package com.eden.orchid.api.resources.resource;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.theme.pages.OrchidReference;
import org.json.JSONObject;

import java.util.Map;

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

    @Override
    protected void loadContent() {
        if(rawContent != null) {
            EdenPair<String, Map<String, Object>> parsedContent = reference.getContext().getEmbeddedData(reference.getExtension(), rawContent);
            this.content = parsedContent.first;
            this.embeddedData = new JSONElement(new JSONObject(parsedContent.second));
        }
        else {
            this.rawContent = "";
            this.content = "";
            this.embeddedData = null;
        }
    }

    @Override
    public void free() {
        rawContent = null;
        content = null;
    }
}
