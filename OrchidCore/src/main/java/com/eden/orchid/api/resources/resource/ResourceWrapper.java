package com.eden.orchid.api.resources.resource;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidReference;
import org.jetbrains.annotations.NotNull;

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
    public boolean shouldPrecompile() {
        return resource.shouldPrecompile();
    }

    @Override
    public boolean shouldRender() {
        return resource.shouldRender();
    }

    @Override
    @NotNull
    public InputStream getContentStream() {
        return resource.getContentStream();
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
    public void free() {
        resource.free();
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
