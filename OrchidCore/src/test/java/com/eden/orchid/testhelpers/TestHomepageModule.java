package com.eden.orchid.testhelpers;

import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.impl.generators.HomepageGenerator;

public class TestHomepageModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, HomepageGenerator.class);
    }

}
