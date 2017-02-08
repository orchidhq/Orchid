package com.eden.orchid.api.registration;

import com.eden.orchid.Orchid;
import com.eden.orchid.OrchidContext;

public interface Contextual {
    default OrchidContext getContext() {
        return Orchid.getContext();
    }

    default OrchidRegistrar getRegistrar() {
        return Orchid.getRegistrar();
    }
}
