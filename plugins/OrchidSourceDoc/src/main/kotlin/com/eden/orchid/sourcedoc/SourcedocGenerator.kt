package com.eden.orchid.sourcedoc

import com.copperleaf.kodiak.common.DocElement
import com.copperleaf.kodiak.common.DocInvoker
import com.copperleaf.kodiak.common.ModuleDoc
import com.eden.common.util.IOStreamUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.api.theme.permalinks.PermalinkStrategy
import com.eden.orchid.sourcedoc.model.SourceDocModel
import com.eden.orchid.sourcedoc.model.SourceDocModuleConfig
import com.eden.orchid.sourcedoc.model.SourceDocModuleModel
import com.eden.orchid.sourcedoc.page.SourceDocModuleHomePage
import com.eden.orchid.sourcedoc.page.SourceDocPage
import com.eden.orchid.sourcedoc.page.SourceDocResource
import com.eden.orchid.utilities.OrchidUtils
import java.io.File
import java.nio.file.Path
import javax.inject.Named

abstract class SourcedocGenerator<U : ModuleDoc>(
    key: String,
    @Named("src") val resourcesDir: String,
    val invoker: DocInvoker<U>,
    private val extractor: OptionsExtractor,
    private val permalinkStrategy: PermalinkStrategy
) : OrchidGenerator<SourceDocModel>(key, PRIORITY_EARLY) {

    companion object {
        const val deprecationWarning = """
            This Javadoc generator is being deprecated in favor of a new, more unified, 
            and more modular code-documentation plugin, OrchidSourceDoc. The new system 
            can be enabled now with the `--experimentalSourceDoc` CLI flag, and the legacy 
            generators will be removed in the next major version.
        """
    }

    private val cacheDir: Path by lazy { OrchidUtils.getCacheDir("sourcedoc-$key") }
    private val outputDir: Path by lazy { OrchidUtils.getTempDir("sourcedoc-$key", true) }

    @Option
    lateinit var modules: MutableList<SourceDocModuleConfig>

    @Option
    @Description("The source directories to document.")
    lateinit var sourceDirs: List<String>

    @Option
    @BooleanDefault(true)
    @Description("Whether to reuse outputs from the cache, or rerun each build")
    var fromCache: Boolean = true

    @Option
    @BooleanDefault(false)
    var showRunnerLogs: Boolean = false

    override fun startIndexing(context: OrchidContext): SourceDocModel {
        val loadedModules = if (modules.size > 1) {
            modules.map {
                setupModule(context, it)
            }
        } else {
            listOf(setupModule(context, null))
        }

        return SourceDocModel(loadedModules)
    }

    override fun startGeneration(context: OrchidContext, model: SourceDocModel) {
        model.allPages.forEach { context.renderTemplate(it) }
    }

// Setup modules and index pages
//----------------------------------------------------------------------------------------------------------------------

    private fun setupModule(context: OrchidContext, config: SourceDocModuleConfig?): SourceDocModuleModel {
        extractor.extractOptions(invoker, allData)

        val moduleName = if (config != null) config.name else ""
        val moduleTitle = if (config != null) config.name else "${key.capitalize()}doc"

        val invokerModel: U? = loadFromCacheOrRun(config)
        val modelPageMap = invokerModel?.let {
            it.nodes.map { node ->
                val nodeName: String = node.prop.name
                val nodeElements: List<DocElement> = node.getter()

                val nodePages = nodeElements.map { element ->
                    SourceDocPage(
                        SourceDocResource(context, element),
                        element,
                        nodeName,
                        element.name,
                        key,
                        moduleName
                    ).also { permalinkStrategy.applyPermalink(it, ":moduleType/:module/:sourceDocPath") }
                }

                node to nodePages
            }.toMap()
        } ?: emptyMap()

        return SourceDocModuleModel(
            setupModuleHomepage(context, config, moduleName, moduleTitle),
            moduleName,
            invokerModel,
            modelPageMap
        )
    }

    private fun setupModuleHomepage(
        context: OrchidContext,
        config: SourceDocModuleConfig?,
        moduleName: String,
        moduleTitle: String
    ): SourceDocModuleHomePage {
        var readmeFile: OrchidResource? = null

        for(baseDir in (config?.sourceDirs ?: sourceDirs)) {
            val baseFile = File(resourcesDir).toPath().resolve(baseDir).toFile().absolutePath
            val closestFile: OrchidResource? = context.findClosestFile(baseFile, "readme", false, 10)
            if(closestFile != null) {
                readmeFile = closestFile
                break
            }
        }

        if(readmeFile == null) {
            readmeFile = StringResource(
                "",
                OrchidReference(
                    context,
                    ""
                )
            )
        }

        readmeFile.reference.outputExtension = "html"

        return SourceDocModuleHomePage(readmeFile, key, moduleTitle, key, moduleName)
            .also { permalinkStrategy.applyPermalink(it, ":moduleType/:module") }
    }

// helpers
//----------------------------------------------------------------------------------------------------------------------

    private fun loadFromCacheOrRun(config: SourceDocModuleConfig?): U? {
        val actualOutputDir = if(config != null) outputDir.resolve(config.name) else outputDir

        if (config?.fromCache ?: fromCache) {
            val moduleDoc = invoker.loadCachedModuleDoc(actualOutputDir)
            if (moduleDoc != null) {
                return moduleDoc
            }
        }

        return invoker.getModuleDoc(
            (config?.sourceDirs ?: sourceDirs).map { File(resourcesDir).toPath().resolve(it) },
            actualOutputDir,
            emptyList()
        ) { inputStream ->
            if (config?.showRunnerLogs ?: showRunnerLogs) {
                IOStreamUtils.InputStreamPrinter(inputStream, null) as Runnable
            } else {
                IOStreamUtils.InputStreamIgnorer(inputStream) as Runnable
            }
        }
    }

}
