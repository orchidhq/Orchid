package com.eden.orchid.server.api;

public interface OrchidController {

    default String getPathNamespace()  {
        return "/" + this.getClass().getSimpleName().toLowerCase().replace("controller", "");
    }
}
