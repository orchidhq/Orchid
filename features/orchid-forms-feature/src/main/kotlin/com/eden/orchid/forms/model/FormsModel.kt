package com.eden.orchid.forms.model

import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage

class FormsModel(
    val forms: List<Form>
) : OrchidGenerator.Model {
    override val allPages: List<OrchidPage> = emptyList()
    override val collections: List<OrchidCollection<*>> = emptyList()

    fun getForm(key: String): Form? {
        return forms.singleOrNull { it.key == key }
    }
}
