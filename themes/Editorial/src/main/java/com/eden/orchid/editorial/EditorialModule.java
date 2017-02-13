package com.eden.orchid.editorial;

import com.eden.orchid.OrchidModule;

public class EditorialModule extends OrchidModule {

    @Override
    protected void configure() {
        addTheme(EditorialTheme.class);
    }
}
