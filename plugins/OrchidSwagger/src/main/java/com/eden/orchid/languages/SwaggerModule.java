package com.eden.orchid.languages;

import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.theme.components.OrchidComponent;

public class SwaggerModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidComponent.class,
                SwaggerComponent.class);
    }
}
