package com.eden.orchid.search

import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.generators.emptyModel
import com.eden.orchid.api.indexing.OrchidIndex
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.render.RenderService
import com.eden.orchid.api.resources.resource.JsonResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.impl.generators.ExternalIndexGenerator

@Description("Generates index files to connect your site to others.", name = "Indices")
class SearchIndexGenerator : OrchidGenerator<OrchidGenerator.Model>(GENERATOR_KEY, Stage.META) {

    companion object {
        const val GENERATOR_KEY = "indices"
    }

    @Option
    @Description("A list of generator keys whose pages are considered in this taxonomy.")
    lateinit var includeFrom: Array<String>

    @Option
    @Description("A list of generator keys whose pages are ignored by this taxonomy.")
    lateinit var excludeFrom: Array<String>

    @Option
    @BooleanDefault(false)
    @Description("Whether to create a JSON file for each page in your site, with a path of the page plus .json")
    var createSinglePageIndexFiles: Boolean = false

    override fun startIndexing(context: OrchidContext): Model {
        return emptyModel()
    }

    override fun startGeneration(context: OrchidContext, model: Model) {
        generateSiteIndexFiles(context)
        if(createSinglePageIndexFiles) {
            generatePageIndexFiles(context)
        }
    }

    private fun generateSiteIndexFiles(context: OrchidContext) {
        val indices = OrchidIndex(null, "index")

        // Render an page for each generator's individual index
        val enabledGeneratorKeys = context.getGeneratorKeys(includeFrom, excludeFrom)

        context.index.allIndexedPages.filter { it.key in enabledGeneratorKeys }.forEach { (key, value) ->
            if(key === ExternalIndexGenerator.GENERATOR_KEY) return@forEach // don't create search indices for externally-indexed pages

            val jsonElement = JSONElement(value.first.toJSON(true, false))
            val reference = OrchidReference(context, "meta/$key.index.json")
            val resource = JsonResource(reference, jsonElement)
            val page = OrchidPage(resource, RenderService.RenderMode.RAW, "index", null)
            page.reference.isUsePrettyUrl = false
            context.render(page)

            indices.addToIndex(indices.ownKey + "/" + page.reference.path, page)
        }

        // Render full composite index page
        val compositeJsonElement = JSONElement(context.index.toJSON(true, false))
        val compositeReference = OrchidReference(context, "meta/all.index.json")
        val compositeIndexResource = JsonResource(compositeReference, compositeJsonElement)
        val compositeIndexPage = OrchidPage(compositeIndexResource, RenderService.RenderMode.RAW, "index", null)
        compositeIndexPage.reference.isUsePrettyUrl = false
        context.render(compositeIndexPage)
        indices.addToIndex(indices.ownKey + "/" + compositeIndexPage.reference.path, compositeIndexPage)

        // Render an index of all indices, so individual index pages can be found
        for (page in indices.allPages) {
            page.data = HashMap()
        }
        val indexResource = JsonResource(OrchidReference(context, "meta/index.json"), JSONElement(indices.toJSON(false, false)))
        val indicesPage = OrchidPage(indexResource, RenderService.RenderMode.RAW, "index", null)
        indicesPage.reference.isUsePrettyUrl = false
        context.render(indicesPage)
    }

    private fun generatePageIndexFiles(context: OrchidContext) {
        context.index.allPages.forEach { page ->
            val jsonElement = JSONElement(page.toJSON(true, true))
            val reference = OrchidReference(page.reference)
            val resource = JsonResource(reference, jsonElement)
            val pageIndex = OrchidPage(resource, RenderService.RenderMode.RAW, "pageIndex", null)
            pageIndex.reference.isUsePrettyUrl = false
            pageIndex.reference.outputExtension = "json"
            context.render(pageIndex)
        }
    }

}
