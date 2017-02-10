package com.eden.orchid.impl;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.resources.OrchidResourceSource;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.impl.resources.OrchidFileResources;

public class MainModule extends OrchidModule {

    @Override
    protected void configure() {
        bind(OrchidResources.class).to(OrchidFileResources.class);

        addToSet(OrchidResourceSource.class, OrchidFileResources.class);
    }
}
