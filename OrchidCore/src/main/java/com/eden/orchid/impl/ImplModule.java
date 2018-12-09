package com.eden.orchid.impl;

import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.compilers.OrchidPrecompiler;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.api.events.OrchidEventListener;
import com.eden.orchid.api.generators.GlobalCollection;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.indexing.IndexServiceImpl;
import com.eden.orchid.api.publication.OrchidPublisher;
import com.eden.orchid.api.registration.IgnoreModule;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.resources.ResourceServiceImpl;
import com.eden.orchid.api.resources.resourceSource.LocalResourceSource;
import com.eden.orchid.api.server.OrchidController;
import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.api.tasks.OrchidCommand;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.TaskServiceImpl;
import com.eden.orchid.api.theme.AdminTheme;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.ThemeServiceImpl;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.menus.OrchidMenuFactory;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.commands.BuildCommand;
import com.eden.orchid.impl.commands.DeployCommand;
import com.eden.orchid.impl.commands.HelpCommand;
import com.eden.orchid.impl.commands.QuitCommand;
import com.eden.orchid.impl.compilers.clog.ClogSetupListener;
import com.eden.orchid.impl.compilers.frontmatter.FrontMatterPrecompiler;
import com.eden.orchid.impl.compilers.markdown.MarkdownCompiler;
import com.eden.orchid.impl.compilers.parsers.CSVParser;
import com.eden.orchid.impl.compilers.parsers.JsonParser;
import com.eden.orchid.impl.compilers.parsers.PropertiesParser;
import com.eden.orchid.impl.compilers.parsers.TOMLParser;
import com.eden.orchid.impl.compilers.parsers.YamlParser;
import com.eden.orchid.impl.compilers.pebble.PebbleCompiler;
import com.eden.orchid.impl.compilers.sass.SassCompiler;
import com.eden.orchid.impl.compilers.text.HtmlCompiler;
import com.eden.orchid.impl.compilers.text.TextCompiler;
import com.eden.orchid.impl.generators.AssetsGenerator;
import com.eden.orchid.impl.generators.HomepageGenerator;
import com.eden.orchid.impl.generators.SitemapGenerator;
import com.eden.orchid.impl.generators.collections.ExternalPageCollection;
import com.eden.orchid.impl.generators.collections.FrontMatterCollection;
import com.eden.orchid.impl.publication.GithubPagesPublisher;
import com.eden.orchid.impl.publication.NetlifyPublisher;
import com.eden.orchid.impl.publication.ScriptPublisher;
import com.eden.orchid.impl.resources.InlineResourceSource;
import com.eden.orchid.impl.resources.LocalFileResourceSource;
import com.eden.orchid.impl.tasks.BuildTask;
import com.eden.orchid.impl.tasks.DeployTask;
import com.eden.orchid.impl.tasks.HelpTask;
import com.eden.orchid.impl.tasks.ServeTask;
import com.eden.orchid.impl.tasks.ShellTask;
import com.eden.orchid.impl.tasks.WatchTask;
import com.eden.orchid.impl.themes.DefaultTheme;
import com.eden.orchid.impl.themes.components.LicenseComponent;
import com.eden.orchid.impl.themes.components.PageContentComponent;
import com.eden.orchid.impl.themes.components.ReadmeComponent;
import com.eden.orchid.impl.themes.components.TemplateComponent;
import com.eden.orchid.impl.themes.functions.AnchorFunction;
import com.eden.orchid.impl.themes.functions.AssetFunction;
import com.eden.orchid.impl.themes.functions.CompileAsFunction;
import com.eden.orchid.impl.themes.functions.FindAllFunction;
import com.eden.orchid.impl.themes.functions.FindFunction;
import com.eden.orchid.impl.themes.functions.LimitToFunction;
import com.eden.orchid.impl.themes.functions.LinkFunction;
import com.eden.orchid.impl.themes.functions.LoadFunction;
import com.eden.orchid.impl.themes.functions.LocalDateFunction;
import com.eden.orchid.impl.themes.functions.ParseAsFunction;
import com.eden.orchid.impl.themes.functions.ResizeFunction;
import com.eden.orchid.impl.themes.functions.RotateFunction;
import com.eden.orchid.impl.themes.functions.ScaleFunction;
import com.eden.orchid.impl.themes.menus.DividerMenuItem;
import com.eden.orchid.impl.themes.menus.GeneratorPagesMenuItem;
import com.eden.orchid.impl.themes.menus.LinkMenuItem;
import com.eden.orchid.impl.themes.menus.PageChildrenMenuItem;
import com.eden.orchid.impl.themes.menus.PageMenuItem;
import com.eden.orchid.impl.themes.menus.PageParentMenuItem;
import com.eden.orchid.impl.themes.menus.PageSiblingsMenuItem;
import com.eden.orchid.impl.themes.menus.PageSubtreeMenuItem;
import com.eden.orchid.impl.themes.menus.SubmenuMenuItem;
import com.eden.orchid.impl.themes.tags.AccordionTag;
import com.eden.orchid.impl.themes.tags.BreadcrumbsTag;
import com.eden.orchid.impl.themes.tags.HeadTag;
import com.eden.orchid.impl.themes.tags.LogTag;
import com.eden.orchid.impl.themes.tags.PageTag;
import com.eden.orchid.impl.themes.tags.ScriptsTag;
import com.eden.orchid.impl.themes.tags.StylesTag;
import com.eden.orchid.impl.themes.tags.TabsTag;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.Provides;
import io.github.classgraph.ClassGraph;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@IgnoreModule
public final class ImplModule extends OrchidModule {

    @Override
    protected void configure() {
        withResources(1);

        // prepare empty sets for binding
        addToSet(OrchidService.class);

        addToSet(GlobalCollection.class,
                FrontMatterCollection.class,
                ExternalPageCollection.class);

        // Themes
        addToSet(Theme.class,
                DefaultTheme.class);

        addToSet(AdminTheme.class);

        // Resource Sources
        addToSet(LocalResourceSource.class,
                LocalFileResourceSource.class,
                InlineResourceSource.class);

        // Compilers
        addToSet(OrchidCompiler.class,
                MarkdownCompiler.class,
                PebbleCompiler.class,
                SassCompiler.class,
                TextCompiler.class,
                HtmlCompiler.class);

        // Parsers
        addToSet(OrchidParser.class,
                CSVParser.class,
                YamlParser.class,
                TOMLParser.class,
                JsonParser.class,
                PropertiesParser.class);

        // Precompilers
        addToSet(OrchidPrecompiler.class,
                FrontMatterPrecompiler.class);

        // Generators
        addToSet(OrchidGenerator.class,
                AssetsGenerator.class,
                HomepageGenerator.class,
                SitemapGenerator.class);

        // Tasks and Commands
        addToSet(OrchidTask.class,
                HelpTask.class,
                BuildTask.class,
                WatchTask.class,
                ServeTask.class,
                DeployTask.class,
                ShellTask.class);

        addToSet(OrchidCommand.class,
                HelpCommand.class,
                BuildCommand.class,
                DeployCommand.class,
                QuitCommand.class);

        // Menu Items
        addToSet(OrchidMenuFactory.class,
                DividerMenuItem.class,
                SubmenuMenuItem.class,
                LinkMenuItem.class,
                GeneratorPagesMenuItem.class,
                PageMenuItem.class,
                PageParentMenuItem.class,
                PageSiblingsMenuItem.class,
                PageChildrenMenuItem.class,
                PageSubtreeMenuItem.class);

        // Component Types
        addToSet(OrchidComponent.class,
                LicenseComponent.class,
                ReadmeComponent.class,
                PageContentComponent.class,
                TemplateComponent.class);

        // Server
        addToSet(OrchidEventListener.class,
                ThemeServiceImpl.class,
                TaskServiceImpl.class,
                ResourceServiceImpl.class,
                IndexServiceImpl.class,
                ClogSetupListener.class);

        addToSet(OrchidController.class);

        // Template Functions
        addToSet(TemplateFunction.class,
                AssetFunction.class,
                AnchorFunction.class,
                CompileAsFunction.class,
                ParseAsFunction.class,
                FindAllFunction.class,
                FindFunction.class,
                LimitToFunction.class,
                LinkFunction.class,
                LoadFunction.class,
                LocalDateFunction.class,

                RotateFunction.class,
                ScaleFunction.class,
                ResizeFunction.class
        );

        // Publication Methods
        addToSet(OrchidPublisher.class,
                ScriptPublisher.class,
                NetlifyPublisher.class,
                GithubPagesPublisher.class
        );

        // Template Tags
        addToSet(TemplateTag.class,
                LogTag.class,
                BreadcrumbsTag.class,
                HeadTag.class,
                PageTag.class,
                ScriptsTag.class,
                StylesTag.class,
                AccordionTag.class,
                TabsTag.class
        );

        addToSet(AdminList.class, new AdminList() {
            @Override
            public String getKey() {
                return "services";
            }

            @Override
            public Class<?> getListClass() {
                return OrchidService.class;
            }

            @Override
            public Collection<Class<?>> getItems() {
                return Orchid
                        .getInstance()
                        .getContext()
                        .getServices()
                        .stream()
                        .map(Object::getClass)
                        .collect(Collectors.toList());
            }

            @Override
            public boolean isImportantType() {
                return true;
            }
        });

        addToSet(AdminList.class, new AdminList() {

            private Collection<Class<?>> pages;

            @Override
            public String getKey() {
                return "pages";
            }

            @Override
            public Class<?> getListClass() {
                return OrchidPage.class;
            }

            @Override
            public Collection<Class<?>> getItems() {
                if (pages == null) {
                    pages = new ArrayList<>();

                    new ClassGraph()
                            .enableClassInfo()
                            .scan()
                            .getSubclasses(OrchidPage.class.getName())
                            .loadClasses(OrchidPage.class)
                            .forEach(pageClass -> {
                                if (!Modifier.isAbstract(pageClass.getModifiers())) {
                                    pages.add(pageClass);
                                }
                            });
                }

                return pages;
            }

            @Override
            public boolean isImportantType() {
                return true;
            }
        });
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkhttpClient(@Named("dest") String destinationDir) throws IOException {
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .cache(new Cache(OrchidUtils.getTempDir(destinationDir, "okHttpCache", true).toFile(), 50 * 1024 * 1024)) // 50 MiB cache
                .build();
    }

}
