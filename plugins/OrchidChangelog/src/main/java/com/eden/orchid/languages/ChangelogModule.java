package com.eden.orchid.languages;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;

public class ChangelogModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, ChangelogGenerator.class);
    }
}
