package com.eden.orchid.sourcedoc.model

import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator

class SourceDocModel(
    val modules: List<SourceDocModuleModel>,
    override val collections: List<OrchidCollection<*>>
) : OrchidGenerator.Model {

    override val allPages by lazy {
        modules.flatMap { it.allPages }
    }

}
