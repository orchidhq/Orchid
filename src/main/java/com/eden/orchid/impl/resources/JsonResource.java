package com.eden.orchid.impl.resources;

import com.eden.common.json.JSONElement;
import com.eden.orchid.resources.OrchidReference;
import com.eden.orchid.resources.OrchidResource;

public final class JsonResource extends OrchidResource {

    public JsonResource(JSONElement element, OrchidReference reference) {
        super(reference);
        if(element != null) {
            this.rawContent = element.toString();
            this.content = element.toString();
            this.embeddedData = null;
        }
        else {
            this.rawContent = "";
            this.content = "";
            this.embeddedData = null;
        }
    }
}
