package com.eden.orchid.javadoc

import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.javadoc.pages.JavadocClassPage
import com.eden.orchid.javadoc.pages.JavadocPackagePage
import java.util.stream.Stream

@Description("A Javadoc Collection represents the pages for all the classes and packages in your Java project. A " +
        "page is matched from a Javadoc Collection with an 'itemId' matching either the simple class name or the " +
        "fully-qualified class or package name."
)
class JavadocCollection(generator: JavadocGenerator, collectionId: String, items: List<OrchidPage>)
    : OrchidCollection<OrchidPage>(generator, collectionId, items) {

    override fun find(id: String): Stream<OrchidPage> {
        if(id.contains('.')) {
            return items.stream()
                    .filter { page ->
                        if (page is JavadocClassPage) {
                            page.classDoc.qualifiedName == id
                        }
                        else if (page is JavadocPackagePage) {
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
                        if (page is JavadocClassPage) {
                            page.classDoc.name == id
                        }
                        else {
                            false
                        }
                    }
        }
    }

}
