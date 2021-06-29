package com.eden.orchid.impl.generators

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.generators.modelOf
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.render.RenderService
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.pages.OrchidReference

@Description(value = "Generates the root homepage for your site.", name = "Homepage")
class HomepageGenerator : OrchidGenerator<OrchidGenerator.Model>(GENERATOR_KEY, Stage.CONTENT) {

    override fun startIndexing(context: OrchidContext): Model {
        return modelOf { listOf(loadHomepage(context), load404page(context)) }
    }

    private fun loadHomepage(context: OrchidContext): OrchidPage {
        var resource: OrchidResource? =
            sequenceOf("homepage", "HOMEPAGE", "Homepage", "HomePage")
                .mapNotNull {
                    context
                        .getFlexibleResourceSource(LocalResourceSource, null)
                        .locateResourceEntry(context, it)
                }
                .firstOrNull()

        if (resource == null) {
            resource = StringResource(OrchidReference(context, "homepage.md"), "")
        }

        val page = Homepage(resource, "frontPage", "Home")
        page.reference.fileName = ""
        return page
    }

    private fun load404page(context: OrchidContext): OrchidPage {
        var resource = context
            .getFlexibleResourceSource(LocalResourceSource, null)
            .locateResourceEntry(context, "404")
        if (resource == null) {
            resource = StringResource(OrchidReference(context, "404.md"), "")
        }

        val page = OrchidPage(resource, RenderService.RenderMode.TEMPLATE, "404", "Not Found")
        page.reference.fileName = "404"
        page.reference.isUsePrettyUrl = false

        return page
    }

    companion object {
        const val GENERATOR_KEY = "home"
    }

    private class Homepage(
        resource: OrchidResource,
        key: String,
        title: String
    ) : OrchidPage(resource, RenderService.RenderMode.TEMPLATE, key, title) {

        override val itemIds = listOf("home", "Home")
    }
}
