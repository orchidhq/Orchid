package com.eden.orchid.impl.resources;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;

import javax.inject.Inject;

public final class CoreResourceSource extends PluginResourceSource {

    @Inject
    public CoreResourceSource(OrchidContext context) {
        super(context, 1);
    }

}
