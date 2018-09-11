package com.eden.orchid.kotlindoc

import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.kotlindoc.page.KotlindocClassPage
import com.eden.orchid.kotlindoc.page.KotlindocPackagePage
import java.util.stream.Stream

@Description("A Kotlindoc Collection represents the pages for all the classes and packages in your Kotlin project. A " +
        "page is matched from a Kotlindoc Collection with an 'itemId' matching either the simple class name or the " +
        "fully-qualified class or package name."
)
class KotlindocCollection(generator: KotlindocGenerator, collectionId: String, items: List<OrchidPage>)
    : OrchidCollection<OrchidPage>(generator, collectionId, items) {

    override fun find(id: String): Stream<OrchidPage> {
        if(id.contains('.')) {
            return items.stream()
                    .filter { page ->
                        if (page is KotlindocClassPage) {
                            page.classDoc.qualifiedName == id
                        }
                        else if (page is KotlindocPackagePage) {
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
                        if (page is KotlindocClassPage) {
                            page.classDoc.name == id
                        }
                        else {
                            false
                        }
                    }
        }
    }

}
