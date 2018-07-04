package com.eden.orchid.impl.compilers.pebble;

import com.eden.orchid.api.events.OrchidEventListener;
import com.eden.orchid.api.registration.OrchidModule;
import com.mitchellbosecke.pebble.attributes.AttributeResolver;
import com.mitchellbosecke.pebble.extension.Extension;

public final class PebbleModule extends OrchidModule {

    @Override
    protected void configure() {
        // Pebble Extensions
        addToSet(AttributeResolver.class);
//        addToSet(AttributeResolver.class,
//                GetMethodAttributeResolver.class);
        addToSet(Extension.class,
                CorePebbleExtension.class);

        addToSet(OrchidEventListener.class,
                PebbleCompiler.class);
    }
}
