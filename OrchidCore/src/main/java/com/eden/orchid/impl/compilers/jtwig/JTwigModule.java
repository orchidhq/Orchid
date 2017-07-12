package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import org.jtwig.functions.JtwigFunction;
import org.jtwig.resource.loader.TypedResourceLoader;

public class JTwigModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(JtwigFunction.class,
                CompileAsFilter.class,
                JsonFilter.class,
                LimitFilter.class,
                SortByFilter.class
        );

        addToSet(TypedResourceLoader.class,
                JTwigResourceLoader.class);

        bind(OrchidRenderer.class).to(TwigRenderer.class);
        bind(TemplateResolutionStrategy.class).to(TwigTemplateResolutionStrategy.class);
    }
}
