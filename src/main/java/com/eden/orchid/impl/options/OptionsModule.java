package com.eden.orchid.impl.options;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.options.OrchidOption;

public class OptionsModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidOption.class, BaseUrlOption.class);
        addToSet(OrchidOption.class, CommentLanguageOption.class);
        addToSet(OrchidOption.class, ConfigOption.class);
        addToSet(OrchidOption.class, DataFilesOption.class);
        addToSet(OrchidOption.class, DestinationOption.class);
        addToSet(OrchidOption.class, DisabledGeneratorsOption.class);
        addToSet(OrchidOption.class, EnvironmentOption.class);
        addToSet(OrchidOption.class, ResourcesOption.class);
        addToSet(OrchidOption.class, ThemeOption.class);
        addToSet(OrchidOption.class, VersionOption.class);
        addToSet(OrchidOption.class, WikiPathOption.class);
    }
}
