package com.eden.orchid.bsdoc;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.theme.Theme;

public class BsDocModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(Theme.class, BSDocTheme.class);
    }
}
