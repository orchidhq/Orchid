package com.eden.orchid.bsdoc;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.compilers.OrchidCompiler;

public class BsDocModule extends OrchidModule {

    @Override
    protected void configure() {
        addTheme(BSDocTheme.class);
        addToSet(OrchidCompiler.class, SassCompiler.class);
    }
}
