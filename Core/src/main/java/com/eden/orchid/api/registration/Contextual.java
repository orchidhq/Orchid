package com.eden.orchid.api.registration;

import com.eden.orchid.Orchid;
import com.eden.orchid.Theme;
import com.eden.orchid.api.OrchidContext;
import com.google.inject.Injector;

public interface Contextual {
    default OrchidContext getContext() {
        return Orchid.getContext();
    }

    default Injector getInjector() {
        return Orchid.getInjector();
    }

    default Theme getTheme() {
        return Orchid.getContext().getTheme();
    }
}
