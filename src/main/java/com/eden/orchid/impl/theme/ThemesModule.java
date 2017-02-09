package com.eden.orchid.impl.theme;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.Theme;
import com.eden.orchid.api.resources.OrchidResourceSource;

public class ThemesModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(Theme.class, BasicTheme.class);
        addToSet(OrchidResourceSource.class, BasicTheme.class);
    }
}
