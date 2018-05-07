package com.eden.orchid.api.resources.resource;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidReference;

import java.io.IOException;
import java.io.InputStream;

/**
 * A Resource type that wraps another resource, optionally applying a transformation along the way.
 */
public class ResourceWrapper extends FreeableResource {

    private final OrchidResource resource;

    public ResourceWrapper(OrchidResource resource) {
        super(resource.getReference());
        this.resource = resource;
    }

    @Override
    public OrchidContext getContext() {
        return resource.getContext();
    }

    @Override
    public OrchidReference getReference() {
        return resource.getReference();
    }

    @Override
    public String getContent() {
        return resource.getContent();
    }

    @Override
    public JSONElement getEmbeddedData() {
        return resource.getEmbeddedData();
    }

    @Override
    public int getPriority() {
        return resource.getPriority();
    }

    @Override
    public void setRawContent(String rawContent) {
        resource.setRawContent(rawContent);
    }

    @Override
    public void setContent(String content) {
        resource.setContent(content);
    }

    @Override
    public void setEmbeddedData(JSONElement embeddedData) {
        resource.setEmbeddedData(embeddedData);
    }

    @Override
    public void setPriority(int priority) {
        resource.setPriority(priority);
    }

    @Override
    public void setShouldRender(boolean shouldRender) {
        resource.setShouldRender(shouldRender);
    }

    @Override
    public JSONElement queryEmbeddedData(String pointer) {
        return resource.queryEmbeddedData(pointer);
    }

    @Override
    public boolean shouldPrecompile() {
        return resource.shouldPrecompile();
    }

    @Override
    public boolean shouldRender() {
        return resource.shouldRender();
    }

    @Override
    public InputStream getContentStream() {
        return resource.getContentStream();
    }

    @Override
    public String getRawContent() {
        return resource.getRawContent();
    }

    @Override
    public String compileContent(Object data) {
        return resource.compileContent(data);
    }

    @Override
    public String getPrecompilerExtension() {
        return resource.getPrecompilerExtension();
    }

    @Override
    public boolean canUpdate() {
        return resource.canUpdate();
    }

    @Override
    public boolean canDelete() {
        return resource.canDelete();
    }

    @Override
    public void update(InputStream newContent) throws IOException {
        resource.update(newContent);
    }

    @Override
    public void delete() throws IOException {
        resource.delete();
    }

    @Override
    public String getTitle() {
        return resource.getTitle();
    }

    @Override
    protected void loadContent() {
        if (resource instanceof FreeableResource) {
            ((FreeableResource) resource).loadContent();
        }
    }

    @Override
    public void free() {
        if (resource instanceof FreeableResource) {
            ((FreeableResource) resource).free();
        }
    }

    @Override
    public String toString() {
        return resource.toString();
    }

    @Override
    public int hashCode() {
        return resource.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return resource.equals(obj);
    }

}
