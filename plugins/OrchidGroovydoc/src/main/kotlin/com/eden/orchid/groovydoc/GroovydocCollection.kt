package com.eden.orchid.groovydoc

import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.groovydoc.pages.GroovydocClassPage
import com.eden.orchid.groovydoc.pages.GroovydocPackagePage
import java.util.stream.Stream

@Description("A groovydoc Collection represents the pages for all the classes and packages in your Groovy project. A " +
        "page is matched from a groovydoc Collection with an 'itemId' matching either the simple class name or the " +
        "fully-qualified class or package name."
)
class GroovydocCollection(generator: GroovydocGenerator, collectionId: String, items: List<OrchidPage>)
    : OrchidCollection<OrchidPage>(generator, collectionId, items) {

    override fun find(id: String): Stream<OrchidPage> {
        if(id.contains('.')) {
            return items.stream()
                    .filter { page ->
                        if (page is GroovydocClassPage) {
                            page.classDoc.qualifiedName == id
                        }
                        else if (page is GroovydocPackagePage) {
                            page.packageDoc.name == id
                        }
                        else {
                            false
                        }
                    }
        }
        else {
            return items.stream()
                    .filter { page ->
                        if (page is GroovydocClassPage) {
                            page.classDoc.name == id
                        }
                        else {
                            false
                        }
                    }
        }
    }

}
