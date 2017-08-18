package com.eden.orchid.api.server;

import com.eden.orchid.api.registration.Prioritized;

public abstract class OrchidController extends Prioritized {

    public String getPathNamespace()  {
        return "/" + this.getClass().getSimpleName().toLowerCase().replace("controller", "");
    }
}
