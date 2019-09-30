package com.eden.orchid.sourcedoc.page

import com.eden.orchid.api.resources.resource.OrchidResource

class SourceDocModuleHomePage(
    resource: OrchidResource,
    key: String,
    title: String,
    moduleType: String,
    moduleGroup: String,
    module: String
) : BaseSourceDocPage(
    resource,
    key,
    title,
    moduleType,
    moduleGroup,
    module
) {

    override fun getTemplates(): List<String> {
        return listOf(
            "${generator.key}Module",
            "sourceDocModule"
        )
    }
}
