package com.eden.orchid.javadoc;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;

import javax.inject.Inject;

public class JavadocResourceSource extends PluginResourceSource {

    @Inject
    public JavadocResourceSource(OrchidContext context) {
        super(context, 10);
    }
}
