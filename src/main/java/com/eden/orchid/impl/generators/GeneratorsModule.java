package com.eden.orchid.impl.generators;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;

public class GeneratorsModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, AssetsGenerator.class);
        addToSet(OrchidGenerator.class, MetadataGenerator.class);
        addToSet(OrchidGenerator.class, PagesGenerator.class);
        addToSet(OrchidGenerator.class, PostsGenerator.class);
        addToSet(OrchidGenerator.class, WikiGenerator.class);
    }
}
