package com.eden.orchid.impl;

import com.caseyjbrooks.clog.Clog;
import com.caseyjbrooks.clog.ClogFormatter;
import com.caseyjbrooks.clog.parseltongue.Parseltongue;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.compilers.OrchidPrecompiler;
import com.eden.orchid.api.events.OrchidEventListener;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.registration.IgnoreModule;
import com.eden.orchid.api.resources.resourceSource.FileResourceSource;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.eden.orchid.api.server.OrchidController;
import com.eden.orchid.api.server.OrchidFileController;
import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.TaskServiceImpl;
import com.eden.orchid.api.theme.AdminTheme;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.impl.compilers.csv.CSVParser;
import com.eden.orchid.impl.compilers.frontmatter.FrontMatterPrecompiler;
import com.eden.orchid.impl.compilers.jtwig.JTwigCompiler;
import com.eden.orchid.impl.compilers.markdown.MarkdownCompiler;
import com.eden.orchid.impl.compilers.parsers.JsonParser;
import com.eden.orchid.impl.compilers.parsers.YamlParser;
import com.eden.orchid.impl.compilers.sass.SassCompiler;
import com.eden.orchid.impl.compilers.text.TextCompiler;
import com.eden.orchid.impl.compilers.toml.TOMLParser;
import com.eden.orchid.impl.generators.AssetsGenerator;
import com.eden.orchid.impl.generators.HomepageGenerator;
import com.eden.orchid.impl.generators.IndexGenerator;
import com.eden.orchid.impl.resources.CoreResourceSource;
import com.eden.orchid.impl.resources.LocalFileResourceSource;
import com.eden.orchid.impl.server.admin.AdminController;
import com.eden.orchid.impl.server.admin.lists.CompilersList;
import com.eden.orchid.impl.server.admin.lists.ComponentsList;
import com.eden.orchid.impl.server.admin.lists.GeneratorsList;
import com.eden.orchid.impl.server.admin.lists.MenuItemsList;
import com.eden.orchid.impl.server.admin.lists.OptionsList;
import com.eden.orchid.impl.server.admin.lists.ParsersList;
import com.eden.orchid.impl.server.admin.lists.ResourceSourcesList;
import com.eden.orchid.impl.server.admin.lists.TasksList;
import com.eden.orchid.impl.server.admin.lists.ThemesList;
import com.eden.orchid.impl.server.files.FileController;
import com.eden.orchid.impl.tasks.BuildTask;
import com.eden.orchid.impl.tasks.ServeTask;
import com.eden.orchid.impl.tasks.WatchTask;
import com.eden.orchid.impl.themes.DefaultAdminTheme;
import com.eden.orchid.impl.themes.DefaultTheme;
import com.eden.orchid.impl.themes.components.LicenseComponent;
import com.eden.orchid.impl.themes.components.PageContentComponent;
import com.eden.orchid.impl.themes.components.ReadmeComponent;
import com.eden.orchid.impl.themes.components.TemplateComponent;
import com.eden.orchid.impl.themes.menus.DividerMenuItem;
import com.eden.orchid.impl.themes.menus.DropdownMenuItem;
import com.eden.orchid.impl.themes.menus.IndexMenuItem;
import com.eden.orchid.impl.themes.menus.LinkMenuItem;
import com.eden.orchid.utilities.ClogSpells;
import com.google.inject.multibindings.Multibinder;

@IgnoreModule
public final class ImplModule extends OrchidModule {

    private static final Class[] optionalSets = new Class[]{
            OrchidService.class
    };

    @Override
    protected void configure() {
        bind(OrchidPrecompiler.class).to(FrontMatterPrecompiler.class);
        bind(OrchidFileController.class).to(FileController.class);

        addToSet(Theme.class, DefaultTheme.class);
        addToSet(AdminTheme.class, DefaultAdminTheme.class);

        for (Class<?> defaultSet : optionalSets) {
            Multibinder.newSetBinder(binder(), defaultSet);
        }

        // Resource Sources
        addToSet(FileResourceSource.class, LocalFileResourceSource.class);
        addToSet(PluginResourceSource.class, CoreResourceSource.class);

        // Compilers
        addToSet(OrchidCompiler.class,
                MarkdownCompiler.class,
                JTwigCompiler.class,
                SassCompiler.class,
                TextCompiler.class);

        addToSet(OrchidParser.class,
                CSVParser.class,
                YamlParser.class,
                TOMLParser.class,
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
                WatchTask.class,
                ServeTask.class);

        // Menu Items
        addToSet(OrchidMenuItem.class,
                DividerMenuItem.class,
                DropdownMenuItem.class,
                LinkMenuItem.class,
                IndexMenuItem.class);

        // Component Types
        addToSet(OrchidComponent.class,
                LicenseComponent.class,
                ReadmeComponent.class,
                PageContentComponent.class,
                TemplateComponent.class);

        // Server
        addToSet(OrchidEventListener.class,
                TaskServiceImpl.class);

        addToSet(OrchidController.class,
                AdminController.class);

        addToSet(AdminList.class,
                CompilersList.class,
                ComponentsList.class,
                GeneratorsList.class,
                OptionsList.class,
                ParsersList.class,
                ResourceSourcesList.class,
                TasksList.class,
                ThemesList.class,
                MenuItemsList.class);

        ClogFormatter formatter = Clog.getFormatter();
        if(formatter instanceof Parseltongue) {
            ((Parseltongue) formatter).findSpells(ClogSpells.class);
        }
    }
}
