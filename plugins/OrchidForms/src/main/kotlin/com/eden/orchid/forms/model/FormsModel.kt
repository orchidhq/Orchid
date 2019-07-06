package com.eden.orchid.forms.model

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage

class FormsModel(
    val forms: MutableMap<String, Form>
) : OrchidGenerator.Model {
    override val allPages: List<OrchidPage> = emptyList()
}
