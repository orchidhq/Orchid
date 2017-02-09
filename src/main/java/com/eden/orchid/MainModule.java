package com.eden.orchid;

import com.eden.orchid.api.registration.OrchidRegistrar;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.impl.registration.Registrar;
import com.eden.orchid.impl.resources.OrchidFileResources;

public class MainModule extends OrchidModule {

    @Override
    protected void configure() {
        bind(OrchidResources.class).to(OrchidFileResources.class);
        bind(OrchidRegistrar.class).to(Registrar.class);
    }
}
