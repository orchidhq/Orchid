package com.eden.orchid.kss;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;

import javax.inject.Inject;

public class KssResourceSource extends PluginResourceSource {

    @Inject
    public KssResourceSource(OrchidContext context) {
        super(context, 20);
    }
}
