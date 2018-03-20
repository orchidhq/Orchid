package com.eden.orchid.javadoc

import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.javadoc.pages.JavadocClassPage
import com.eden.orchid.javadoc.pages.JavadocPackagePage

class JavadocCollection(generator: JavadocGenerator, collectionId: String, items: List<OrchidPage>)
    : OrchidCollection<OrchidPage>(generator, "javadoc", collectionId, items) {

    override fun find(id: String): List<OrchidPage> {
        if(id.contains('.')) {
            return items
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
                    .toList()
        }
        else {
            return items
                    .filter { page ->
                        if (page is JavadocClassPage) {
                            page.classDoc.name() == id
                        }
                        else {
                            false
                        }
                    }
                    .toList()
        }
    }

}
