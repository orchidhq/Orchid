package com.eden.orchid.sourcedoc.model

import com.eden.orchid.api.generators.OrchidGenerator

class SourceDocModel(
    val modules: List<SourceDocModuleModel>
) : OrchidGenerator.Model {

    override val allPages by lazy {
        modules.flatMap { it.allPages }
    }

}
