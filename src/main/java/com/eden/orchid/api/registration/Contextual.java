package com.eden.orchid.api.registration;

import com.eden.orchid.Orchid;
import com.eden.orchid.OrchidContext;
import com.google.inject.Injector;

public interface Contextual {
    default OrchidContext getContext() {
        return Orchid.getContext();
    }

    default OrchidRegistrar getRegistrar() {
        return getInjector().getInstance(OrchidRegistrar.class);
    }

    default Injector getInjector() {
        return Orchid.getInjector();
    }
}
