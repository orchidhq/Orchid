package com.eden.orchid.javadoc

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.theme.pages.OrchidPage
import java.util.stream.Stream

class ExternalJavadocCollection(val context: OrchidContext, generator: JavadocGenerator)
    : OrchidCollection<OrchidPage>(generator, null, ArrayList()) {

    override fun find(id: String): Stream<OrchidPage> {
        val page = context.externalIndex.findPage(id.replace("\\.".toRegex(), "/"))
        return (if(page != null) listOf(page) else emptyList()).stream()
    }

}
