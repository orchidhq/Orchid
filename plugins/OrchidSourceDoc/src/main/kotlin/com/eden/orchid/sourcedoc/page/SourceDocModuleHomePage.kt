package com.eden.orchid.sourcedoc.page

import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.sourcedoc.SourcedocGenerator

class SourceDocModuleHomePage(
    resource: OrchidResource,
    val sourcedocGenerator: SourcedocGenerator<*>
) : OrchidPage(
    resource,
    "${sourcedocGenerator.key}Module",
    "${sourcedocGenerator.key.capitalize()} Modules"
) {

    override fun getTemplates(): List<String> {
        return listOf(
            "${sourcedocGenerator.key}Module",
            "sourceDocModule"
        )
    }
}
