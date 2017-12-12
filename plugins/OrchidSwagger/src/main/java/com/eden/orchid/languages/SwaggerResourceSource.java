package com.eden.orchid.languages;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;

import javax.inject.Inject;

public class SwaggerResourceSource extends PluginResourceSource {

    @Inject
    public SwaggerResourceSource(OrchidContext context) {
        super(context, 100);
    }

}
