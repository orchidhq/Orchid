package com.eden.orchid.posts;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.impl.compilers.jtwig.CompileAsFilter;
import org.jtwig.functions.JtwigFunction;

public class PostsModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, PostsGenerator.class);

        addToSet(JtwigFunction.class, SortPostsFilter.class);
    }
}
