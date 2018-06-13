package com.eden.orchid.pages

import com.eden.orchid.api.generators.FolderCollection
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.pages.pages.StaticPage
import java.util.stream.Stream

class StaticPageGroupCollection(generator: PagesGenerator, collectionId: String, items: List<OrchidPage>)
    : FolderCollection(generator, collectionId, items, StaticPage::class.java, "${generator.baseDir}/$collectionId") {

    override fun find(id: String): Stream<OrchidPage> {
        return items.stream().filter { page -> page.title == id }
    }

}
