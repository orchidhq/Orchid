package com.eden.orchid.html5up.futureimperfect;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.theme.Theme;

public class FutureImperfectModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(Theme.class, FutureImperfectTheme.class);
    }
}
