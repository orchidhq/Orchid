package com.eden.orchid.materialize;

import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.theme.Theme;

public class MaterializeModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(Theme.class, MaterializeTheme.class);
    }
}
