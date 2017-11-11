package com.eden.orchid.languages.asciidoc;

import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.registration.OrchidModule;

public class AsciidocModule extends OrchidModule {

    @Override
    protected void configure() {
        // Compilers
        addToSet(OrchidCompiler.class,
                AsciiDoctorCompiler.class);
    }
}
