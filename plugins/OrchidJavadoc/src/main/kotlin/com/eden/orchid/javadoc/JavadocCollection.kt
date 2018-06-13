package com.eden.orchid.javadoc

import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.javadoc.pages.JavadocClassPage
import com.eden.orchid.javadoc.pages.JavadocPackagePage
import java.util.stream.Stream

class JavadocCollection(generator: JavadocGenerator, collectionId: String, items: List<OrchidPage>)
    : OrchidCollection<OrchidPage>(generator, collectionId, items) {

    override fun find(id: String): Stream<OrchidPage> {
        if(id.contains('.')) {
            return items.stream()
                    .filter { page ->
                        if (page is JavadocClassPage) {
                            page.classDoc.qualifiedName() == id
                        }
                        else if (page is JavadocPackagePage) {
                            page.packageDoc.name() == id
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
                            page.classDoc.name() == id
                        }
                        else {
                            false
                        }
                    }
        }
    }

}
