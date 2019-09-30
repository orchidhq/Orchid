package com.eden.orchid.taxonomies.permalink.pathtype

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.taxonomies.pages.CollectionArchivePage
import javax.inject.Inject

class CollectionTypePathType
@Inject
constructor() : PermalinkPathType(100) {

    override fun acceptsKey(page: OrchidPage, key: String): Boolean {
        return key == "collectionType" && page is CollectionArchivePage
    }

    override fun format(page: OrchidPage, key: String): String? {
        return (page as CollectionArchivePage).collectionArchive.collectionType
    }

}

