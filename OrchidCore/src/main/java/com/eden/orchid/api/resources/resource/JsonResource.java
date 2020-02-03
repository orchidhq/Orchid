package com.eden.orchid.api.resources.resource;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.utilities.OrchidExtensionsKt;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * A Resource type that provides a JSON data as content to a template. When used with renderTemplate() or renderString(),
 * this resource will supply additional data to the template renderer. When used with renderRaw(), the raw JSON-encoded
 * data will be written directly instead.
 */
public final class JsonResource extends OrchidResource {

    private final JSONElement hardcodedJson;

    public JsonResource(JSONElement element, OrchidReference reference) {
        super(reference);
        this.hardcodedJson = element;
    }

    @NotNull
    @Override
    public InputStream getContentStream() {
        String contentJson = "";
        if(hardcodedJson.getElement() instanceof JSONObject) {
            contentJson = ((JSONObject) hardcodedJson.getElement()).toString(2);
        }
        else if(hardcodedJson.getElement() instanceof JSONArray) {
            contentJson = ((JSONArray) hardcodedJson.getElement()).toString(2);
        }
        else{
            contentJson = hardcodedJson.toString();
        }

        return OrchidExtensionsKt.asInputStream(contentJson);
    }
}
