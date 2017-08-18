package com.eden.orchid.impl;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.CompilerService;
import com.eden.orchid.api.compilers.CompilerServiceImpl;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.compilers.OrchidPrecompiler;
import com.eden.orchid.api.events.EventService;
import com.eden.orchid.api.events.EventServiceImpl;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.indexing.IndexService;
import com.eden.orchid.api.indexing.IndexServiceImpl;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.OptionsService;
import com.eden.orchid.api.options.OptionsServiceImpl;
import com.eden.orchid.api.options.extractors.BooleanOptionExtractor;
import com.eden.orchid.api.options.extractors.DoubleOptionExtractor;
import com.eden.orchid.api.options.extractors.FloatOptionExtractor;
import com.eden.orchid.api.options.extractors.IntOptionExtractor;
import com.eden.orchid.api.options.extractors.JSONArrayOptionExtractor;
import com.eden.orchid.api.options.extractors.JSONObjectOptionExtractor;
import com.eden.orchid.api.options.extractors.LongOptionExtractor;
import com.eden.orchid.api.options.extractors.OptionsHolderOptionExtractor;
import com.eden.orchid.api.options.extractors.StringOptionExtractor;
import com.eden.orchid.api.resources.ResourceService;
import com.eden.orchid.api.resources.ResourceServiceImpl;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.api.resources.resourceSource.LocalResourceSource;
import com.eden.orchid.api.server.OrchidController;
import com.eden.orchid.api.server.OrchidFileController;
import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.TaskService;
import com.eden.orchid.api.tasks.TaskServiceImpl;
import com.eden.orchid.api.theme.ThemeService;
import com.eden.orchid.api.theme.ThemeServiceImpl;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import com.eden.orchid.impl.compilers.frontmatter.FrontMatterPrecompiler;
import com.eden.orchid.impl.compilers.jtwig.JTwigCompiler;
import com.eden.orchid.impl.compilers.markdown.MarkdownCompiler;
import com.eden.orchid.impl.compilers.parsers.JsonParser;
import com.eden.orchid.impl.compilers.parsers.YamlParser;
import com.eden.orchid.impl.compilers.sass.SassCompiler;
import com.eden.orchid.impl.generators.AssetsGenerator;
import com.eden.orchid.impl.generators.HomepageGenerator;
import com.eden.orchid.impl.generators.IndexGenerator;
import com.eden.orchid.impl.resources.CoreDefaultResourceSource;
import com.eden.orchid.impl.resources.CoreLocalResourceSource;
import com.eden.orchid.impl.server.admin.AdminController;
import com.eden.orchid.impl.server.admin.lists.CompilersList;
import com.eden.orchid.impl.server.admin.lists.GeneratorsList;
import com.eden.orchid.impl.server.admin.lists.OptionsList;
import com.eden.orchid.impl.server.admin.lists.ParsersList;
import com.eden.orchid.impl.server.admin.lists.ResourceSourcesList;
import com.eden.orchid.impl.server.admin.lists.TasksList;
import com.eden.orchid.impl.server.admin.lists.ThemesList;
import com.eden.orchid.impl.server.files.FileController;
import com.eden.orchid.impl.tasks.BuildTask;
import com.eden.orchid.impl.tasks.ListCompilersTask;
import com.eden.orchid.impl.tasks.ListGeneratorsTask;
import com.eden.orchid.impl.tasks.ListOptionsTask;
import com.eden.orchid.impl.tasks.ListResourceSourcesTask;
import com.eden.orchid.impl.tasks.ListTasksTask;
import com.eden.orchid.impl.tasks.ListThemesTask;
import com.eden.orchid.impl.tasks.ServeTask;
import com.eden.orchid.impl.themes.LocalTheme;
import com.eden.orchid.impl.themes.menus.DividerMenuItem;
import com.eden.orchid.impl.themes.menus.IndexMenuItem;
import com.eden.orchid.impl.themes.menus.LinkMenuItem;
import com.google.inject.multibindings.Multibinder;

import java.util.EventListener;

public class ImplModule extends OrchidModule {

    private static final Class[] optionalSets = new Class[]{};

    @Override
    protected void configure() {
        bind(OrchidPrecompiler.class).to(FrontMatterPrecompiler.class);
        bind(OrchidFileController.class).to(FileController.class);

        bind(CompilerService.class).to(CompilerServiceImpl.class);
        bind(ThemeService.class).to(ThemeServiceImpl.class);
        bind(EventService.class).to(EventServiceImpl.class);
        bind(IndexService.class).to(IndexServiceImpl.class);
        bind(ResourceService.class).to(ResourceServiceImpl.class);
        bind(TaskService.class).to(TaskServiceImpl.class);
        bind(OptionsService.class).to(OptionsServiceImpl.class);

        bind(OrchidContext.class).to(OrchidContextImpl.class);

        addTheme(LocalTheme.class);

        for (Class<?> defaultSet : optionalSets) {
            Multibinder.newSetBinder(binder(), defaultSet);
        }

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
                ServeTask.class,
                BuildTask.class,
                ListCompilersTask.class,
                ListGeneratorsTask.class,
                ListOptionsTask.class,
                ListResourceSourcesTask.class,
                ListTasksTask.class,
                ListThemesTask.class);

        // Menu Items
        addToMap(OrchidMenuItemFactory.class, "separator", DividerMenuItem.class);
        addToMap(OrchidMenuItemFactory.class, "link", LinkMenuItem.class);
        addToMap(OrchidMenuItemFactory.class, "index", IndexMenuItem.class);

        // OptionsExtractors
        addToSet(OptionExtractor.class,
                StringOptionExtractor.class,
                IntOptionExtractor.class,
                LongOptionExtractor.class,
                FloatOptionExtractor.class,
                DoubleOptionExtractor.class,
                BooleanOptionExtractor.class,
                OptionsHolderOptionExtractor.class,
                JSONObjectOptionExtractor.class,
                JSONArrayOptionExtractor.class);

        // Server
        addToSet(EventListener.class,
                ServeTask.class);

        addToSet(OrchidController.class,
                AdminController.class);

        addToSet(AdminList.class,
                CompilersList.class,
                GeneratorsList.class,
                OptionsList.class,
                ParsersList.class,
                ResourceSourcesList.class,
                TasksList.class,
                ThemesList.class);
    }
}
