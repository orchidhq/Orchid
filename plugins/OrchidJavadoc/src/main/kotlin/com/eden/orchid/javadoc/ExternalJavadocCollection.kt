package com.eden.orchid.javadoc

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.pages.OrchidPage
import java.util.stream.Stream

@Description("A External Javadoc Collection represents the pages for all the classes and packages in another Orchid " +
        "site that is being referenced from yours. A page is matched from an External Javadoc Collection with an " +
        "'itemId' matching the fully-qualified class name."
)
class ExternalJavadocCollection(val context: OrchidContext, generator: JavadocGenerator)
    : OrchidCollection<OrchidPage>(generator, null, ArrayList()) {

    override fun find(id: String): Stream<OrchidPage> {
        val page = context.externalIndex.findPage(id.replace("\\.".toRegex(), "/"))
        return (if(page != null) listOf(page) else emptyList()).stream()
    }

}
