package com.eden.orchid.impl.resources;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.FileResourceSource;
import com.google.inject.name.Named;

import javax.inject.Inject;

public final class LocalFileResourceSource extends FileResourceSource {

    private final String resourcesDir;

    @Inject
    public LocalFileResourceSource(OrchidContext context, @Named("resourcesDir") String resourcesDir) {
        super(context, Integer.MAX_VALUE);
        this.resourcesDir = resourcesDir;
    }

    @Override
    public String getDirectory() {
        return this.resourcesDir;
    }
}
