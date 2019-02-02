package com.eden.orchid.pages

import com.eden.orchid.api.generators.FolderCollection
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.pages.pages.StaticPage

@Description(
    "A Static Page Group Collection is a specialized Folder collection for a single page Group. A page is " +
            "matched from a this collection with an 'itemId' matching the page's title."
)
class StaticPageGroupCollection(
    generator: PagesGenerator,
    collectionId: String,
    items: List<OrchidPage>
) : FolderCollection(generator, collectionId, items, StaticPage::class.java, "${generator.baseDir}/$collectionId")
