package com.eden.orchid.wiki;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.render.ContentFilter;

public class WikiModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, WikiGenerator.class);
        addToSet(ContentFilter.class, WikiGlossaryTermFilter.class);
    }
}
