package com.eden.orchid.pluginDocs;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;

import javax.inject.Inject;

public class PluginDocsResourceSource extends PluginResourceSource {

    @Inject
    public PluginDocsResourceSource(OrchidContext context) {
        super(context, 1000);
    }

}
