package com.eden.orchid.bsdoc;

import com.eden.orchid.OrchidModule;

public class BsDocModule extends OrchidModule {

    @Override
    protected void configure() {
        addTheme(BSDocTheme.class);
    }
}
