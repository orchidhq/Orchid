package com.eden.orchid.api.resources;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.registration.Contextual;

public abstract class OrchidResource implements Contextual {

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

}
