package com.eden.orchid.languages;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;

public class PagesModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, PagesGenerator.class);
    }
}
