package com.eden.orchid.api.options;

import com.eden.orchid.api.OrchidContext;

import javax.inject.Inject;

public class OrchidOptions {

    private OrchidContext context;

    @Inject
    public OrchidOptions(OrchidContext context) {
        this.context = context;
    }

    public void loadOptions() {

    }
}
