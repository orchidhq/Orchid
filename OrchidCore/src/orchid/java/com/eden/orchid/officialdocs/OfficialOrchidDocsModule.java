package com.eden.orchid.officialdocs;

import com.eden.orchid.api.events.OrchidEventListener;
import com.eden.orchid.api.registration.OrchidModule;

public class OfficialOrchidDocsModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidEventListener.class,
                OfficialOrchidDocsListener.class);

    }
}
