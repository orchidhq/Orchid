package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.api.registration.OrchidModule;
import org.jtwig.functions.JtwigFunction;
import org.jtwig.resource.loader.TypedResourceLoader;

public final class JTwigModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(JtwigFunction.class,
                CompileAsFilter.class,
                JsonFilter.class,
                LimitFilter.class,
                SortByFilter.class,
                TemplateFilter.class,
                AlertFilter.class
        );

        addToSet(TypedResourceLoader.class,
                JTwigResourceLoader.class);
    }
}
