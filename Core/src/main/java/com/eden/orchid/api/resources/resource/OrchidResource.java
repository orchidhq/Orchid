package com.eden.orchid.api.resources.resource;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidReference;
import lombok.Data;

@Data
public abstract class OrchidResource {

    protected OrchidContext context;
    protected OrchidReference reference;

    protected String rawContent;
    protected String content;
    protected JSONElement embeddedData;

    protected int priority;

    protected boolean shouldPrecompile;

    public OrchidResource(OrchidReference reference) {
        if (reference == null) {
            throw new IllegalArgumentException("A resource must have a valid OrchidReference");
        }
        else {
            this.context = reference.getContext();
            this.reference = reference;
            this.shouldPrecompile = false;
        }
    }

    public JSONElement queryEmbeddedData(String pointer) {
        if (embeddedData != null) {
            return embeddedData.query(pointer);
        }

        return null;
    }

    public boolean shouldPrecompile() {
        return shouldPrecompile;
    }
}
