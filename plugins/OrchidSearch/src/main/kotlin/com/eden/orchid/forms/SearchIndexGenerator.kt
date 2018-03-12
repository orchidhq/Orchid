package com.eden.orchid.forms;

import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.indexing.OrchidInternalIndex
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.JsonResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.pages.OrchidReference
import org.json.JSONObject
import java.util.stream.Stream
import javax.inject.Inject

@Description("Generates index files to connect your site to others.")
class SearchIndexGenerator @Inject
constructor(context: OrchidContext) : OrchidGenerator(context, generatorKey, OrchidGenerator.PRIORITY_LATE) {

    companion object {
        const val generatorKey = "indices"
    }

    @Option
    @BooleanDefault(false)
    @Description("Whether this generator is enabled or not. This generator effectively doubles the number of pages " +
            "generated, but allows most pages on your site to "
    )
    var enablePageIndices: Boolean = false

    override fun startIndexing(): kotlin.collections.List<OrchidPage>? {
        return null
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        generateSiteIndexFiles()
        if(enablePageIndices) {
            generatePageIndexFiles()
        }
    }

    private fun generateSiteIndexFiles() {
        val internalIndex = context.internalIndex
        val mappedIndex = internalIndex.allIndexedPages

        val indices = OrchidInternalIndex("index")

        // Render an page for each generator's individual index
        mappedIndex.keys.forEach { key ->
            val jsonElement = JSONElement(mappedIndex[key]!!.toJSON(true, false))
            val reference = OrchidReference(context, "meta/$key.index.json")
            val resource = JsonResource(jsonElement, reference)
            val page = OrchidPage(resource, "index")
            page.reference.isUsePrettyUrl = false
            context.renderRaw(page)

            indices.addToIndex(indices.ownKey + "/" + page.reference.path, page)
        }

        // Render full composite index page
        val compositeJsonElement = JSONElement(internalIndex.toJSON(true, false))
        val compositeReference = OrchidReference(context, "meta/all.index.json")
        val compositeIndexResource = JsonResource(compositeJsonElement, compositeReference)
        val compositeIndexPage = OrchidPage(compositeIndexResource, "index")
        compositeIndexPage.reference.isUsePrettyUrl = false
        context.renderRaw(compositeIndexPage)
        indices.addToIndex(indices.ownKey + "/" + compositeIndexPage.reference.path, compositeIndexPage)

        // Render an index of all indices, so individual index pages can be found
        for (page in indices.allPages) {
            page.data = JSONObject()
        }
        val indexResource = JsonResource(JSONElement(indices.toJSON(false, false)), OrchidReference(context, "meta/index.json"))
        val indicesPage = OrchidPage(indexResource, "index")
        indicesPage.reference.isUsePrettyUrl = false
        context.renderRaw(indicesPage)
    }

    private fun generatePageIndexFiles() {
        context.internalIndex.allPages.forEach { page ->
            val jsonElement = JSONElement(page.toJSON(true, true))
            val reference = OrchidReference(page.reference)
            val resource = JsonResource(jsonElement, reference)
            val pageIndex = OrchidPage(resource, "pageIndex")
            pageIndex.reference.isUsePrettyUrl = true
            pageIndex.reference.outputExtension = "json"
            context.renderRaw(pageIndex)
        }
    }

    override fun getCollections(): kotlin.collections.List<OrchidCollection<*>>? {
        return null
    }

}
