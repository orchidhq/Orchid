package com.eden.orchid.presentations.model

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage

class PresentationsModel(
    val presentations: Map<String, Presentation>
) : OrchidGenerator.Model {

    override val allPages: List<OrchidPage> = emptyList()
}
