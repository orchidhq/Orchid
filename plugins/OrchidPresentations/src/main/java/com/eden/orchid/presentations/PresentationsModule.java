package com.eden.orchid.presentations;

import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.eden.orchid.api.theme.components.OrchidComponent;

public class PresentationsModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(PluginResourceSource.class,
                PresentationsResourceSource.class);

        addToSet(OrchidGenerator.class,
                PresentationsGenerator.class);

        addToSet(OrchidComponent.class,
                PresentationComponent.class);
    }

}
