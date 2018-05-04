package com.eden.orchid.swiftdoc

import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.swiftdoc.page.SwiftdocStatementPage

class SwiftdocCollection(generator: SwiftdocGenerator, collectionId: String, items: List<SwiftdocStatementPage>)
    : OrchidCollection<OrchidPage>(generator, "swiftdoc", collectionId, items) {

    override fun find(id: String): List<OrchidPage> {
        return items
                .filter { page ->
                    if (page is SwiftdocStatementPage) {
                        page.statement.name == id.trimEnd('?')
                    }
                    else {
                        false
                    }
                }
                .toList()
    }

}
