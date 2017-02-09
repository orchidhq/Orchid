package com.eden.orchid.impl.compilers;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidPreCompiler;
import com.eden.orchid.impl.compilers.jtwig.JTwigCompiler;

public class CompilersModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidCompiler.class, MarkdownCompiler.class);
        addToSet(OrchidCompiler.class, JTwigCompiler.class);

        addToSet(OrchidPreCompiler.class, FrontMatterPrecompiler.class);
    }
}
