package com.eden.orchid.presentations;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FormsResourceSource extends PluginResourceSource {

    @Inject
    public FormsResourceSource(OrchidContext context) {
        super(context, 20);
    }

}
