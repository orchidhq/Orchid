package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.OrchidModule;
import org.jtwig.functions.JtwigFunction;
import org.jtwig.resource.loader.TypedResourceLoader;

public class JTwigModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(JtwigFunction.class,
                CompileAsFilter.class,
                JsonFilter.class,
                LimitFilter.class,
                QueryFilter.class,
                SortByFilter.class,
                WalkMapFilter.class);

        addToSet(TypedResourceLoader.class,
                JTwigResourceLoader.class);
    }
}
