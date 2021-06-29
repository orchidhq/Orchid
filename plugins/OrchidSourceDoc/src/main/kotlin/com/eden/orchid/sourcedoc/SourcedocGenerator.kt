package com.eden.orchid.sourcedoc

import com.copperleaf.kodiak.common.AutoDocumentNode
import com.copperleaf.kodiak.common.DocElement
import com.copperleaf.kodiak.common.DocInvoker
import com.copperleaf.kodiak.common.ModuleDoc
import com.eden.common.util.EdenUtils
import com.eden.common.util.IOStreamUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.generators.PageCollection
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.api.theme.permalinks.PermalinkStrategy
import com.eden.orchid.sourcedoc.model.SourceDocModel
import com.eden.orchid.sourcedoc.model.SourceDocModuleConfig
import com.eden.orchid.sourcedoc.model.SourceDocModuleModel
import com.eden.orchid.sourcedoc.page.SourceDocModuleHomePage
import com.eden.orchid.sourcedoc.page.SourceDocPage
import com.eden.orchid.sourcedoc.page.SourceDocResource
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.SuppressedWarnings
import java.io.File
import java.nio.file.Path

abstract class SourcedocGenerator<T : ModuleDoc, U : SourceDocModuleConfig>(
    key: String,
    val invoker: DocInvoker<T>,
    private val extractor: OptionsExtractor,
    private val permalinkStrategy: PermalinkStrategy
) : OrchidGenerator<SourceDocModel>(key, Stage.CONTENT) {

    private val cacheDir: Path by lazy { OrchidUtils.getCacheDir("sourcedoc-$key") }
    private val outputDir: Path by lazy { OrchidUtils.getTempDir("sourcedoc-$key", true) }

    abstract var modules: MutableList<U>
    abstract var defaultConfig: U

    override fun startIndexing(context: OrchidContext): SourceDocModel {
        if (EdenUtils.isEmpty(modules)) {
            modules.add(defaultConfig)
        }

        val mappedModules = modules.map { setupModule(context, it) }

        return SourceDocModel(mappedModules, getCollections(mappedModules))
    }

    private fun getCollections(modules: List<SourceDocModuleModel>): List<OrchidCollection<*>> {
        return mutableListOf<OrchidCollection<*>>().apply {
            val self = this@SourcedocGenerator

            // create a collection containing all module landing pages
            add(PageCollection(self, "modules", modules.map { it.homepage }))

            modules
                .groupBy { it.moduleGroup }
                .filterKeys { it.isNotBlank() }
                .forEach { (moduleGroup, modulesInGroup) ->
                    add(PageCollection(self, "modules-$moduleGroup", modulesInGroup.map { it.homepage }))
                }

            if (modules.size > 1) {
                modules.forEach { module ->
                    // create a collection containing all pages from a module, excluding the homepage (doc pages only)
                    add(PageCollection(self, module.name, module.nodes.values.flatten()))

                    module.nodes.forEach { (node, nodePages) ->
                        // create a collection for each top-level node in this module
                        add(PageCollection(self, "${module.name}-${node.name}", nodePages))
                    }
                }
            } else {
                val module = modules.single()
                // create a collection containing all pages from a module, excluding the homepage (doc pages only)
                add(PageCollection(self, key, module.nodes.values.flatten()))

                module.nodes.forEach { (node, nodePages) ->
                    // create a collection for each top-level node in this module
                    add(PageCollection(self, node.name, nodePages))
                }
            }
        }
    }

// Setup modules and index pages
// ---------------------------------------------------------------------------------------------------------------------

    private fun setupModule(context: OrchidContext, config: U): SourceDocModuleModel {
        extractor.extractOptions(invoker, allData)

        val invokerModel: T?
        val modelPageMap: Map<AutoDocumentNode, List<SourceDocPage<*>>>
        if (config.homePageOnly) {
            invokerModel = null
            modelPageMap = emptyMap()
        } else {
            invokerModel = loadFromCacheOrRun(context, config)
            modelPageMap = invokerModel?.let {
                it.nodes.map { node ->
                    val nodeName: String = node.name
                    val nodeElements: List<DocElement> = node.elements

                    val nodePages = nodeElements.map { element ->
                        SourceDocPage(
                            SourceDocResource(context, element),
                            element,
                            nodeName,
                            element.name,
                            key,
                            config.moduleGroup,
                            config.name,
                            config.slug
                        ).also { permalinkStrategy.applyPermalink(it, config.sourcePagePermalink) }
                    }

                    node to nodePages
                }.toMap()
            } ?: emptyMap()
        }

        val moduleHomePage = setupModuleHomepage(context, invokerModel, config, config.name, config.slug, config.name)

        modelPageMap.values.flatten().forEach { it.parent = moduleHomePage }

        return SourceDocModuleModel(
            moduleHomePage,
            config.name,
            invokerModel,
            config.moduleGroup,
            config.relatedModules,
            modelPageMap
        )
    }

    @Suppress(SuppressedWarnings.DEPRECATION)
    private fun setupModuleHomepage(
        context: OrchidContext,
        module: T?,
        config: U,
        moduleName: String,
        moduleSlug: String,
        moduleTitle: String
    ): SourceDocModuleHomePage {
        var readmeFile: OrchidResource? = null

        for (baseDir in config.sourceDirs) {
            val baseFile = File(context.sourceDir).toPath().resolve(baseDir).toFile().absolutePath
            val closestFile: OrchidResource? = context
                .getFlexibleResourceSource(LocalResourceSource, null)
                .findClosestFile(context, "readme", baseFile)
            if (closestFile != null) {
                readmeFile = closestFile
                break
            }
        }

        if (readmeFile == null) {
            readmeFile = StringResource(
                OrchidReference(
                    context,
                    ""
                ),
                ""
            )
        }

        readmeFile.reference.outputExtension = "html"

        return SourceDocModuleHomePage(
            readmeFile,
            key,
            moduleTitle,
            key,
            config.moduleGroup,
            moduleName,
            moduleSlug,
            module?.roots()?.map { it.id } ?: emptyList()
        ).also { permalinkStrategy.applyPermalink(it, config.homePagePermalink) }
    }

// helpers
// ---------------------------------------------------------------------------------------------------------------------

    private fun loadFromCacheOrRun(context: OrchidContext, config: U): T? {
        val actualOutputDir = outputDir.resolve(config.name)

        if (config.fromCache) {
            val moduleDoc = invoker.loadCachedModuleDoc(actualOutputDir)
            if (moduleDoc != null) {
                return moduleDoc
            }
        }

        return invoker.getModuleDoc(
            config.sourceDirs.map { File(context.sourceDir).toPath().resolve(it) },
            actualOutputDir,
            config.additionalRunnerArgs()
        ) { inputStream ->
            if (config.showRunnerLogs) {
                val r1: Runnable = IOStreamUtils.InputStreamPrinter(inputStream, null)
                r1
            } else {
                val r2: Runnable = IOStreamUtils.InputStreamIgnorer(inputStream)
                r2
            }
        }
    }
}
