package com.eden.orchid.impl.compilers.pebble

import com.eden.orchid.api.events.OrchidEventListener
import com.eden.orchid.api.registration.OrchidModule
import com.mitchellbosecke.pebble.attributes.AttributeResolver
import com.mitchellbosecke.pebble.extension.Extension

class PebbleModule : OrchidModule() {

    override fun configure() {
        // Pebble Extensions
        addToSet(AttributeResolver::class.java)
        //        addToSet(AttributeResolver.class,
        //                GetMethodAttributeResolver.class);
        addToSet(
            Extension::class.java,
            CorePebbleExtension::class.java
        )

        addToSet(
            OrchidEventListener::class.java,
            PebbleCompiler::class.java
        )
    }
}
