package com.eden.orchid.pages

import com.eden.orchid.api.generators.FileCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage

class StaticPageGroupCollection(generator: OrchidGenerator, collectionId: String, items: List<OrchidPage>)
    : FileCollection(generator, collectionId, items) {

    override fun find(id: String): List<OrchidPage> {
        return items
                .filter { page -> page.title == id }
                .toList()
    }

}
