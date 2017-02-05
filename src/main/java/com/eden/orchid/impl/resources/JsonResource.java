package com.eden.orchid.impl.resources;

import com.eden.common.json.JSONElement;
import com.eden.orchid.resources.OrchidReference;
import com.eden.orchid.resources.OrchidResource;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A Resource type that provides a JSON data as content to a template. When used with renderTemplate() or renderString(),
 * this resource will supply additional data to the template renderer. When used with renderRaw(), the raw JSON-encoded
 * data will be written directly instead.
 */
public final class JsonResource extends OrchidResource {

    public JsonResource(JSONElement element, OrchidReference reference) {
        super(reference);
        if(element != null) {
            if(element.getElement() instanceof JSONObject) {
                this.rawContent = ((JSONObject) element.getElement()).toString(2);
                this.content = ((JSONObject) element.getElement()).toString(2);
                this.embeddedData = element;
            }
            else if(element.getElement() instanceof JSONArray) {
                this.rawContent = ((JSONArray) element.getElement()).toString(2);
                this.content = ((JSONArray) element.getElement()).toString(2);
                this.embeddedData = element;
            }
            else{
                this.rawContent = element.toString();
                this.content = element.toString();
                this.embeddedData = element;
            }
        }
        else {
            this.rawContent = "";
            this.content = "";
            this.embeddedData = null;
        }
    }
}
