package com.eden.orchid.sourcedoc

import com.copperleaf.kodiak.common.DocElement
import com.copperleaf.kodiak.common.DocInvoker
import com.copperleaf.kodiak.common.ModuleDoc
import com.eden.common.util.EdenUtils
import com.eden.common.util.IOStreamUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OptionsExtractor
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

abstract class SourcedocGenerator<T : ModuleDoc, U : SourceDocModuleConfig>(
    key: String,
    @Named("src") val resourcesDir: String,
    val invoker: DocInvoker<T>,
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

    abstract var modules: MutableList<U>
    abstract var defaultConfig: U

    override fun startIndexing(context: OrchidContext): SourceDocModel {
        if (EdenUtils.isEmpty(modules)) {
            modules.add(defaultConfig)
        }

        return SourceDocModel(modules.map { setupModule(context, it) })
    }

    override fun startGeneration(context: OrchidContext, model: SourceDocModel) {
        model.allPages.forEach { context.renderTemplate(it) }
    }

// Setup modules and index pages
//----------------------------------------------------------------------------------------------------------------------

    private fun setupModule(context: OrchidContext, config: U): SourceDocModuleModel {
        extractor.extractOptions(invoker, allData)

        val moduleName = config.name
        val moduleTitle = config.name

        val invokerModel: T? = loadFromCacheOrRun(config)
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
        config: U,
        moduleName: String,
        moduleTitle: String
    ): SourceDocModuleHomePage {
        var readmeFile: OrchidResource? = null

        for (baseDir in config.sourceDirs) {
            val baseFile = File(resourcesDir).toPath().resolve(baseDir).toFile().absolutePath
            val closestFile: OrchidResource? = context.findClosestFile(baseFile, "readme", false, 10)
            if (closestFile != null) {
                readmeFile = closestFile
                break
            }
        }

        if (readmeFile == null) {
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

    private fun loadFromCacheOrRun(config: U): T? {
        val actualOutputDir = outputDir.resolve(config.name)

        if (config.fromCache) {
            val moduleDoc = invoker.loadCachedModuleDoc(actualOutputDir)
            if (moduleDoc != null) {
                return moduleDoc
            }
        }

        return invoker.getModuleDoc(
            config.sourceDirs.map { File(resourcesDir).toPath().resolve(it) },
            actualOutputDir,
            config.additionalRunnerArgs()
        ) { inputStream ->
            if (config.showRunnerLogs) {
                IOStreamUtils.InputStreamPrinter(inputStream, null) as Runnable
            } else {
                IOStreamUtils.InputStreamIgnorer(inputStream) as Runnable
            }
        }
    }

}
