package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.OrchidModule;
import org.jtwig.functions.JtwigFunction;
import org.jtwig.resource.loader.TypedResourceLoader;

public class JTwigModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(JtwigFunction.class,
                TwigCompileAsFilter.class,
                TwigJsonFilter.class,
                TwigLinkFilter.class,
                TwigQueryFilter.class,
                TwigSortByFilter.class,
                TwigWalkMapFilter.class);

        addToSet(TypedResourceLoader.class,
                OrchidResourceLoader.class);
    }
}
