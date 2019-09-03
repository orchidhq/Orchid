package com.eden.orchid.sourcedoc.page

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.sourcedoc.SourcedocGenerator
import com.eden.orchid.sourcedoc.model.SourceDocModuleModel

class SourceDocModulesPage(
    context: OrchidContext,
    val sourcedocGenerator: SourcedocGenerator<*>,
    val modules: List<SourceDocModuleModel>
) : OrchidPage(
    StringResource(
        context,
        "${sourcedocGenerator.key}.md",
        ""
    ),
    "${sourcedocGenerator.key}Modules",
    "${sourcedocGenerator.key.capitalize()} Modules"
) {

    override fun getTemplates(): List<String> {
        return listOf(
            "${sourcedocGenerator.key}Modules",
            "sourceDocModules"
        )
    }
}
