package com.eden.orchid.impl.resources;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.LocalResourceSource;
import com.google.inject.name.Named;

import javax.inject.Inject;

public class CoreLocalResourceSource extends LocalResourceSource {

    private final String resourcesDir;

    @Inject
    public CoreLocalResourceSource(OrchidContext context, @Named("resourcesDir") String resourcesDir) {
        super(context);
        setPriority(Integer.MAX_VALUE);
        this.resourcesDir = resourcesDir;
    }

    @Override
    public String getDirectory() {
        return this.resourcesDir;
    }
}
