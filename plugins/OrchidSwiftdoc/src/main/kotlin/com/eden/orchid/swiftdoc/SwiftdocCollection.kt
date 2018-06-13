package com.eden.orchid.swiftdoc

import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.swiftdoc.page.SwiftdocStatementPage
import java.util.stream.Stream

class SwiftdocCollection(generator: SwiftdocGenerator, collectionId: String, items: List<SwiftdocStatementPage>)
    : OrchidCollection<OrchidPage>(generator, collectionId, items) {

    override fun find(id: String): Stream<OrchidPage> {
        return items.stream()
                .filter { page ->
                    if (page is SwiftdocStatementPage) {
                        page.statement.name == id.trimEnd('?')
                    }
                    else {
                        false
                    }
                }
    }

}
