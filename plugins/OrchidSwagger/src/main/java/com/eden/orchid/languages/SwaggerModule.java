package com.eden.orchid.languages;

import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.languages.components.SwaggerComponent;

public class SwaggerModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(PluginResourceSource.class,
                SwaggerResourceSource.class);

        addToSet(OrchidComponent.class,
                SwaggerComponent.class);
    }
}
