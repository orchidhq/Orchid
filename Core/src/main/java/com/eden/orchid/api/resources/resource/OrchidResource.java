package com.eden.orchid.api.resources.resource;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidReference;

public abstract class OrchidResource {

    protected OrchidContext context;
    protected OrchidReference reference;

    protected String rawContent;
    protected String content;
    protected JSONElement embeddedData;

    protected int priority;

    public OrchidResource(OrchidReference reference) {
        if (reference == null) {
            throw new IllegalArgumentException("A resource must have a valid OrchidReference");
        }
        else {
            this.context = reference.getContext();
            this.reference = reference;
        }
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
        if (embeddedData != null) {
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

    public OrchidContext getContext() {
        return context;
    }
}
