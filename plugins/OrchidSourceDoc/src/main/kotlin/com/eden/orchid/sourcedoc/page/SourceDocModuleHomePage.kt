package com.eden.orchid.sourcedoc.page

import com.eden.orchid.api.resources.resource.OrchidResource

class SourceDocModuleHomePage(
    resource: OrchidResource,
    key: String,
    title: String,
    moduleType: String,
    moduleGroup: String,
    module: String,
    moduleSlug: String,
    val roots: List<String>
) : BaseSourceDocPage(
    resource,
    key,
    title,
    moduleType,
    moduleGroup,
    module,
    moduleSlug
) {

    override fun getTemplates(): List<String> {
        return mutableListOf<String>().also {
            if (moduleGroup.isNotBlank()) {
                it.add("${generator.key}Module-$moduleGroup")
            }
            it.add("${generator.key}Module")
            it.add("sourceDocModuleHomepage")
        }
    }
}
