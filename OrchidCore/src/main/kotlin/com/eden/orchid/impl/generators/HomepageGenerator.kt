package com.eden.orchid.impl.generators

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.FileCollection
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidPage
import java.util.stream.Stream
import javax.inject.Inject

@Description(value = "Generates the root homepage for your site.", name = "Homepage")
class HomepageGenerator
@Inject
constructor(context: OrchidContext) : OrchidGenerator(context, GENERATOR_KEY, PRIORITY_EARLY) {

    override fun startIndexing(): List<OrchidPage> {
        return listOf(loadHomepage(), load404page())
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach { context.renderTemplate(it) }
    }

    override fun getCollections(pages: List<OrchidPage>): List<OrchidCollection<*>> {
        return listOf(FileCollection(this, GENERATOR_KEY, pages))
    }

    private fun loadHomepage(): OrchidPage {
        var resource: OrchidResource? = context.locateLocalResourceEntry("homepage")
        if (resource == null) {
            resource = StringResource(context, "homepage.md", "")
        }

        val page = OrchidPage(resource, "frontPage", context.site.siteInfo.siteName)
        page.reference.fileName = ""
        return page
    }

    private fun load404page(): OrchidPage {
        var resource = context.locateLocalResourceEntry("404")
        if (resource == null) {
            resource = StringResource(context, "404.md", "")
        }

        val page = OrchidPage(resource, "404", context.site.siteInfo.siteName)
        page.reference.fileName = "404"
        page.reference.isUsePrettyUrl = false

        return page
    }

    companion object {
        const val GENERATOR_KEY = "home"
    }

}
