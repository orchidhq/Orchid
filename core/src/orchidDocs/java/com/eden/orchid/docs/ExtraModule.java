package com.eden.orchid.docs;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;

public class ExtraModule extends OrchidModule {
    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, ExtraGenerator.class);
    }
}
