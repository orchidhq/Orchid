package com.eden.orchid.impl.generators

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.FileCollection
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.generators.modelOf
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidPage

@Description(value = "Generates the root homepage for your site.", name = "Homepage")
class HomepageGenerator : OrchidGenerator<OrchidGenerator.Model>(GENERATOR_KEY, PRIORITY_EARLY) {

    override fun startIndexing(context: OrchidContext): Model {
        return modelOf { listOf(loadHomepage(context), load404page(context)) }
    }

    override fun startGeneration(context: OrchidContext, model: Model) {
        model.allPages.forEach { context.renderTemplate(it) }
    }

    override fun getCollections(
        context: OrchidContext,
        model: Model
    ): List<OrchidCollection<*>> {
        return listOf(FileCollection(this, GENERATOR_KEY, model.allPages))
    }

    private fun loadHomepage(context: OrchidContext): OrchidPage {
        var resource: OrchidResource? = context.locateLocalResourceEntry("homepage")
        if (resource == null) {
            resource = StringResource(context, "homepage.md", "")
        }

        val page = OrchidPage(resource, "frontPage", "Home")
        page.reference.fileName = ""
        return page
    }

    private fun load404page(context: OrchidContext): OrchidPage {
        var resource = context.locateLocalResourceEntry("404")
        if (resource == null) {
            resource = StringResource(context, "404.md", "")
        }

        val page = OrchidPage(resource, "404", "Not Found")
        page.reference.fileName = "404"
        page.reference.isUsePrettyUrl = false

        return page
    }

    companion object {
        const val GENERATOR_KEY = "home"
    }

}
