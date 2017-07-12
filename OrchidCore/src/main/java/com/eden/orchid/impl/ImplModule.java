package com.eden.orchid.impl;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.compilers.OrchidPrecompiler;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.render.ContentFilter;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.api.resources.resourceSource.LocalResourceSource;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.menus.OrchidMenuItemType;
import com.eden.orchid.impl.compilers.frontmatter.FrontMatterPrecompiler;
import com.eden.orchid.impl.compilers.jtwig.JTwigCompiler;
import com.eden.orchid.impl.compilers.markdown.MarkdownCompiler;
import com.eden.orchid.impl.compilers.parsers.JsonParser;
import com.eden.orchid.impl.compilers.parsers.YamlParser;
import com.eden.orchid.impl.compilers.sass.SassCompiler;
import com.eden.orchid.impl.events.SetupEnvironment;
import com.eden.orchid.impl.generators.AssetsGenerator;
import com.eden.orchid.impl.generators.HomepageGenerator;
import com.eden.orchid.impl.generators.IndexGenerator;
import com.eden.orchid.impl.resources.CoreDefaultResourceSource;
import com.eden.orchid.impl.resources.CoreLocalResourceSource;
import com.eden.orchid.impl.tasks.BuildTask;
import com.eden.orchid.impl.tasks.ListCompilersTask;
import com.eden.orchid.impl.tasks.ListGeneratorsTask;
import com.eden.orchid.impl.tasks.ListOptionsTask;
import com.eden.orchid.impl.tasks.ListResourceSourcesTask;
import com.eden.orchid.impl.tasks.ListTasksTask;
import com.eden.orchid.impl.tasks.ListThemesTask;
import com.eden.orchid.impl.themes.menus.DividerMenuItem;
import com.google.inject.multibindings.Multibinder;

import java.util.EventListener;

public class ImplModule extends OrchidModule {

    private static final Class[] optionalSets = new Class[] {
            EventListener.class,
            Theme.class,
            ContentFilter.class
    };

    @Override
    protected void configure() {
        bind(OrchidContext.class).to(OrchidContextImpl.class);
        bind(OrchidPrecompiler.class).to(FrontMatterPrecompiler.class);

        for(Class<?> defaultSet : optionalSets) {
            Multibinder.newSetBinder(binder(), defaultSet);
        }

        addToSet(EventListener.class, SetupEnvironment.class);

        // Resource Sources
        addToSet(LocalResourceSource.class, CoreLocalResourceSource.class);
        addToSet(DefaultResourceSource.class, CoreDefaultResourceSource.class);


        // Compilers
        addToSet(OrchidCompiler.class,
                MarkdownCompiler.class,
                JTwigCompiler.class,
                SassCompiler.class);

        addToSet(OrchidParser.class,
                YamlParser.class,
                JsonParser.class);

        // Precompilers
        addToSet(OrchidPrecompiler.class,
                FrontMatterPrecompiler.class);

        // Generators
        addToSet(OrchidGenerator.class,
                AssetsGenerator.class,
                HomepageGenerator.class,
                IndexGenerator.class);

        // Tasks
        addToSet(OrchidTask.class,
                BuildTask.class,
                ListCompilersTask.class,
                ListGeneratorsTask.class,
                ListOptionsTask.class,
                ListResourceSourcesTask.class,
                ListTasksTask.class,
                ListThemesTask.class);

        // Menu Items
        addToMap(OrchidMenuItemType.class, "separator", DividerMenuItem.class);
    }
}
