package com.eden.orchid.editorial;

import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.theme.Theme;

public class EditorialModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(Theme.class, EditorialTheme.class);
    }
}
