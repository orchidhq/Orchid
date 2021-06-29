package com.eden.orchid.taxonomies.permalink.pathtype

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.taxonomies.pages.CollectionArchivePage
import javax.inject.Inject

class CollectionIdPathType
@Inject
constructor() : PermalinkPathType() {

    override fun acceptsKey(page: OrchidPage, key: String): Boolean {
        return key == "collectionId" && page is CollectionArchivePage
    }

    override fun format(page: OrchidPage, key: String): String? {
        return (page as CollectionArchivePage).collectionArchive.collectionId
    }
}
