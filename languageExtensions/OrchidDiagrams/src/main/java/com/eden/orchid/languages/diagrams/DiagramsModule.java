package com.eden.orchid.languages.diagrams;

import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.registration.OrchidModule;

public class DiagramsModule extends OrchidModule {

    @Override
    protected void configure() {
        // Compilers
        addToSet(OrchidCompiler.class,
                PlantUmlCompiler.class);
    }
}
