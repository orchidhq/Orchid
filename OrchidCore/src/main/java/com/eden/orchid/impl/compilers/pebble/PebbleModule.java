package com.eden.orchid.impl.compilers.pebble;

import com.eden.orchid.api.events.OrchidEventListener;
import com.eden.orchid.api.registration.OrchidModule;
import com.mitchellbosecke.pebble.extension.Extension;

public final class PebbleModule extends OrchidModule {

    @Override
    protected void configure() {
        // Pebble Extensions
        addToSet(Extension.class,
                PebbleFunctionsExtension.class,
                PebbleTagsExtension.class);

        addToSet(OrchidEventListener.class,
                PebbleCompiler.class);
    }
}
