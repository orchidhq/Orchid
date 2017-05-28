package com.eden.orchid.api.resources.resource;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.theme.pages.OrchidReference;

/**
 * An ExternalResource is little more than a placeholder Resource designed exclusively for ExternalPages. Much of the
 * functionality for Resources is disabled for this resource type, and is intended just to be used so external pages can be
 * linked to in the same way as internal pages.
 */
public final class ExternalResource extends OrchidResource {

    public ExternalResource(OrchidReference reference) {
        super(reference);
    }

    @Override
    public String getRawContent() {
        throw new UnsupportedOperationException("This method is not allowed on ExternalResource");
    }

    @Override
    public String getContent() {
        throw new UnsupportedOperationException("This method is not allowed on ExternalResource");
    }

    @Override
    public int getPriority() {
        throw new UnsupportedOperationException("This method is not allowed on ExternalResource");
    }

    @Override
    public boolean isShouldPrecompile() {
        throw new UnsupportedOperationException("This method is not allowed on ExternalResource");
    }

    @Override
    public void setReference(OrchidReference reference) {
        throw new UnsupportedOperationException("This method is not allowed on ExternalResource");
    }

    @Override
    public void setRawContent(String rawContent) {
        throw new UnsupportedOperationException("This method is not allowed on ExternalResource");
    }

    @Override
    public void setContent(String content) {
        throw new UnsupportedOperationException("This method is not allowed on ExternalResource");
    }

    @Override
    public void setEmbeddedData(JSONElement embeddedData) {
        throw new UnsupportedOperationException("This method is not allowed on ExternalResource");
    }

    @Override
    public void setPriority(int priority) {
        throw new UnsupportedOperationException("This method is not allowed on ExternalResource");
    }

    @Override
    public void setShouldPrecompile(boolean shouldPrecompile) {
        throw new UnsupportedOperationException("This method is not allowed on ExternalResource");
    }
}
