package com.eden.orchid.sourcedoc.model

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage

class SourceDocModel(
    val indexPage: OrchidPage?,
    val modules: List<SourceDocModuleModel>
) : OrchidGenerator.Model {

    override val allPages by lazy {
        modules.flatMap { it.allPages } + listOfNotNull(indexPage)
    }

}
