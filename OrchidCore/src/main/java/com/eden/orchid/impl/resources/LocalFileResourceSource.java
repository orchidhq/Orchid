package com.eden.orchid.impl.resources;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.FileResourceSource;
import com.eden.orchid.api.resources.resourceSource.LocalResourceSource;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import javax.inject.Inject;

public final class LocalFileResourceSource extends FileResourceSource implements LocalResourceSource {

    @Inject
    public LocalFileResourceSource(Provider<OrchidContext> context, @Named("src") String resourcesDir) {
        super(context, resourcesDir, Integer.MAX_VALUE);
    }

}
