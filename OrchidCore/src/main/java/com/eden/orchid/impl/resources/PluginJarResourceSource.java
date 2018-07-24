package com.eden.orchid.impl.resources;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.resources.resourceSource.JarResourceSource;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.google.inject.Provider;

public final class PluginJarResourceSource extends JarResourceSource implements PluginResourceSource {

    public PluginJarResourceSource(Provider<OrchidContext> context, Class<? extends OrchidModule> moduleClass, int priority) {
        super(context, moduleClass, priority);
    }

}
