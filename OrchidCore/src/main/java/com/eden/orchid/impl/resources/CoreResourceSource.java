package com.eden.orchid.impl.resources;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.JarResourceSource;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.google.inject.Provider;

import javax.inject.Inject;

public final class CoreResourceSource extends JarResourceSource implements PluginResourceSource {

    @Inject
    public CoreResourceSource(Provider<OrchidContext> context) {
        super(context, 1);
    }

}
