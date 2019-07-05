package com.eden.orchid.impl

import com.eden.orchid.Orchid
import com.eden.orchid.api.OrchidService
import com.eden.orchid.api.compilers.OrchidCompiler
import com.eden.orchid.api.compilers.OrchidParser
import com.eden.orchid.api.compilers.OrchidPrecompiler
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.events.OrchidEventListener
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.indexing.IndexServiceImpl
import com.eden.orchid.api.publication.OrchidPublisher
import com.eden.orchid.api.registration.IgnoreModule
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.ResourceServiceImpl
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.server.OrchidController
import com.eden.orchid.api.server.admin.AdminList
import com.eden.orchid.api.tasks.OrchidCommand
import com.eden.orchid.api.tasks.OrchidTask
import com.eden.orchid.api.tasks.TaskServiceImpl
import com.eden.orchid.api.theme.AdminTheme
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.api.theme.ThemeServiceImpl
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.impl.commands.BuildCommand
import com.eden.orchid.impl.commands.DeployCommand
import com.eden.orchid.impl.commands.HelpCommand
import com.eden.orchid.impl.commands.QuitCommand
import com.eden.orchid.impl.compilers.clog.ClogSetupListener
import com.eden.orchid.impl.compilers.frontmatter.FrontMatterPrecompiler
import com.eden.orchid.impl.compilers.markdown.MarkdownCompiler
import com.eden.orchid.impl.compilers.parsers.CSVParser
import com.eden.orchid.impl.compilers.parsers.JsonParser
import com.eden.orchid.impl.compilers.parsers.PropertiesParser
import com.eden.orchid.impl.compilers.parsers.TOMLParser
import com.eden.orchid.impl.compilers.parsers.YamlParser
import com.eden.orchid.impl.compilers.pebble.PebbleCompiler
import com.eden.orchid.impl.compilers.sass.SassCompiler
import com.eden.orchid.impl.compilers.text.HtmlCompiler
import com.eden.orchid.impl.compilers.text.TextCompiler
import com.eden.orchid.impl.generators.AssetsGenerator
import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.impl.generators.SitemapGenerator
import com.eden.orchid.impl.publication.ScriptPublisher
import com.eden.orchid.impl.resources.InlineResourceSource
import com.eden.orchid.impl.resources.LocalFileResourceSource
import com.eden.orchid.impl.tasks.BuildTask
import com.eden.orchid.impl.tasks.DeployTask
import com.eden.orchid.impl.tasks.HelpTask
import com.eden.orchid.impl.tasks.ServeTask
import com.eden.orchid.impl.tasks.ShellTask
import com.eden.orchid.impl.tasks.WatchTask
import com.eden.orchid.impl.themes.DefaultTheme
import com.eden.orchid.impl.themes.components.LicenseComponent
import com.eden.orchid.impl.themes.components.PageContentComponent
import com.eden.orchid.impl.themes.components.ReadmeComponent
import com.eden.orchid.impl.themes.components.TemplateComponent
import com.eden.orchid.impl.themes.functions.AnchorFunction
import com.eden.orchid.impl.themes.functions.AssetFunction
import com.eden.orchid.impl.themes.functions.BaseUrlFunction
import com.eden.orchid.impl.themes.functions.CompileAsFunction
import com.eden.orchid.impl.themes.functions.FindAllFunction
import com.eden.orchid.impl.themes.functions.FindFunction
import com.eden.orchid.impl.themes.functions.HomepageUrlFunction
import com.eden.orchid.impl.themes.functions.LimitToFunction
import com.eden.orchid.impl.themes.functions.LinkFunction
import com.eden.orchid.impl.themes.functions.LoadFunction
import com.eden.orchid.impl.themes.functions.LocalDateFunction
import com.eden.orchid.impl.themes.functions.ParseAsFunction
import com.eden.orchid.impl.themes.functions.ResizeFunction
import com.eden.orchid.impl.themes.functions.RotateFunction
import com.eden.orchid.impl.themes.functions.ScaleFunction
import com.eden.orchid.impl.themes.menus.DividerMenuItem
import com.eden.orchid.impl.themes.menus.GeneratorPagesMenuItem
import com.eden.orchid.impl.themes.menus.LinkMenuItem
import com.eden.orchid.impl.themes.menus.PageChildrenMenuItem
import com.eden.orchid.impl.themes.menus.PageMenuItem
import com.eden.orchid.impl.themes.menus.PageParentMenuItem
import com.eden.orchid.impl.themes.menus.PageSiblingsMenuItem
import com.eden.orchid.impl.themes.menus.PageSubtreeMenuItem
import com.eden.orchid.impl.themes.menus.SubmenuMenuItem
import com.eden.orchid.impl.themes.tags.AccordionTag
import com.eden.orchid.impl.themes.tags.BreadcrumbsTag
import com.eden.orchid.impl.themes.tags.HeadTag
import com.eden.orchid.impl.themes.tags.LogTag
import com.eden.orchid.impl.themes.tags.PageTag
import com.eden.orchid.impl.themes.tags.ScriptsTag
import com.eden.orchid.impl.themes.tags.StylesTag
import com.eden.orchid.impl.themes.tags.TabsTag
import com.eden.orchid.utilities.OrchidUtils
import com.google.inject.Provides
import io.github.classgraph.ClassGraph
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.IOException
import java.lang.reflect.Modifier
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@IgnoreModule
class ImplModule : OrchidModule() {

    override fun configure() {
        withResources(1)

        ClogSetupListener.registerJavaLoggingHandler()

        // prepare empty sets for binding
        addToSet(OrchidService::class.java)

        // Themes
        addToSet(
            Theme::class.java,
            DefaultTheme::class.java
        )

        addToSet(AdminTheme::class.java)

        // Resource Sources
        addToSet(
            LocalResourceSource::class.java,
            LocalFileResourceSource::class.java,
            InlineResourceSource::class.java
        )

        // Compilers
        addToSet(
            OrchidCompiler::class.java,
            MarkdownCompiler::class.java,
            PebbleCompiler::class.java,
            SassCompiler::class.java,
            TextCompiler::class.java,
            HtmlCompiler::class.java
        )

        // Parsers
        addToSet(
            OrchidParser::class.java,
            CSVParser::class.java,
            YamlParser::class.java,
            TOMLParser::class.java,
            JsonParser::class.java,
            PropertiesParser::class.java
        )

        // Precompilers
        addToSet(
            OrchidPrecompiler::class.java,
            FrontMatterPrecompiler::class.java
        )

        // Generators
        addToSet(
            OrchidGenerator::class.java,
            AssetsGenerator::class.java,
            HomepageGenerator::class.java,
            SitemapGenerator::class.java
        )

        // Tasks and Commands
        addToSet(
            OrchidTask::class.java,
            HelpTask::class.java,
            BuildTask::class.java,
            WatchTask::class.java,
            ServeTask::class.java,
            DeployTask::class.java,
            ShellTask::class.java
        )

        addToSet(
            OrchidCommand::class.java,
            HelpCommand::class.java,
            BuildCommand::class.java,
            DeployCommand::class.java,
            QuitCommand::class.java
        )

        // Menu Items
        addToSet(
            OrchidMenuFactory::class.java,
            DividerMenuItem::class.java,
            SubmenuMenuItem::class.java,
            LinkMenuItem::class.java,
            GeneratorPagesMenuItem::class.java,
            PageMenuItem::class.java,
            PageParentMenuItem::class.java,
            PageSiblingsMenuItem::class.java,
            PageChildrenMenuItem::class.java,
            PageSubtreeMenuItem::class.java
        )

        // Component Types
        addToSet(
            OrchidComponent::class.java,
            LicenseComponent::class.java,
            ReadmeComponent::class.java,
            PageContentComponent::class.java,
            TemplateComponent::class.java
        )

        // Server
        addToSet(
            OrchidEventListener::class.java,
            ThemeServiceImpl::class.java,
            TaskServiceImpl::class.java,
            ResourceServiceImpl::class.java,
            IndexServiceImpl::class.java,
            ClogSetupListener::class.java
        )

        addToSet(OrchidController::class.java)

        // Template Functions
        addToSet(
            TemplateFunction::class.java,
            AssetFunction::class.java,
            AnchorFunction::class.java,
            BaseUrlFunction::class.java,
            CompileAsFunction::class.java,
            HomepageUrlFunction::class.java,
            ParseAsFunction::class.java,
            FindAllFunction::class.java,
            FindFunction::class.java,
            LimitToFunction::class.java,
            LinkFunction::class.java,
            LoadFunction::class.java,
            LocalDateFunction::class.java,

            RotateFunction::class.java,
            ScaleFunction::class.java,
            ResizeFunction::class.java
        )

        // Publication Methods
        addToSet(
            OrchidPublisher::class.java,
            ScriptPublisher::class.java
        )

        // Template Tags
        addToSet(
            TemplateTag::class.java,
            LogTag::class.java,
            BreadcrumbsTag::class.java,
            HeadTag::class.java,
            PageTag::class.java,
            ScriptsTag::class.java,
            StylesTag::class.java,
            AccordionTag::class.java,
            TabsTag::class.java
        )

        addToSet(AdminList::class.java, object : AdminList {
            override fun getKey(): String {
                return "services"
            }

            override fun getListClass(): Class<*> {
                return OrchidService::class.java
            }

            override fun getItems(): Collection<Class<*>> {
                return Orchid
                    .getInstance()
                    .context
                    .services
                    .map { it.javaClass }
            }

            override fun isImportantType(): Boolean {
                return true
            }
        })

        addToSet(AdminList::class.java, object : AdminList {

            private var pages: MutableCollection<Class<*>>? = null

            override fun getKey(): String {
                return "pages"
            }

            override fun getListClass(): Class<*> {
                return OrchidPage::class.java
            }

            override fun getItems(): Collection<Class<*>>? {
                if (pages == null) {
                    pages = ArrayList()

                    ClassGraph()
                        .enableClassInfo()
                        .scan()
                        .getSubclasses(OrchidPage::class.java.name)
                        .loadClasses(OrchidPage::class.java)
                        .forEach { pageClass ->
                            if (!Modifier.isAbstract(pageClass.modifiers)) {
                                pages!!.add(pageClass)
                            }
                        }
                }

                return pages
            }

            override fun isImportantType(): Boolean {
                return true
            }
        })
    }

    @Provides
    @Singleton
    @Throws(IOException::class)
    fun provideOkhttpClient(@Named("dest") destinationDir: String): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .cache(
                Cache(
                    OrchidUtils.getTempDir(destinationDir, "okHttpCache", true).toFile(),
                    (50 * 1024 * 1024).toLong()
                )
            ) // 50 MiB cache
            .build()
    }

}
