package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.OrchidModule;
import org.jtwig.functions.JtwigFunction;
import org.jtwig.resource.loader.TypedResourceLoader;

public class JTwigModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(JtwigFunction.class, TwigCompileAsFilter.class);
        addToSet(JtwigFunction.class, TwigJsonFilter.class);
        addToSet(JtwigFunction.class, TwigLinkFilter.class);
        addToSet(JtwigFunction.class, TwigQueryFilter.class);
        addToSet(JtwigFunction.class, TwigSortByFilter.class);
        addToSet(JtwigFunction.class, TwigWalkMapFilter.class);

        addToSet(TypedResourceLoader.class, OrchidResourceLoader.class);
    }
}
