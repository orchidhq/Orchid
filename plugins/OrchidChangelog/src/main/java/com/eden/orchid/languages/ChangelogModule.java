package com.eden.orchid.languages;

import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.languages.components.ChangelogVersionPicker;

public class ChangelogModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class,
                ChangelogGenerator.class);

        addToSet(OrchidComponent.class,
                ChangelogVersionPicker.class);
    }
}
