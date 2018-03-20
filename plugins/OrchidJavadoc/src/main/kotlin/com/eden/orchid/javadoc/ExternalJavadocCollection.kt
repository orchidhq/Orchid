package com.eden.orchid.javadoc

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.theme.pages.OrchidPage

class ExternalJavadocCollection(val context: OrchidContext, generator: JavadocGenerator)
    : OrchidCollection<OrchidPage>(generator, "javadoc", null, ArrayList()) {

    override fun find(id: String): List<OrchidPage> {
        val page = context.externalIndex.findPage(id.replace("\\.".toRegex(), "/"))
        return if(page != null) listOf(page) else emptyList()
    }

}
