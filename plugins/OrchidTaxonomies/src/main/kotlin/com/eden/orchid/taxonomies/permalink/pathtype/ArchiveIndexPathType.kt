package com.eden.orchid.taxonomies.permalink.pathtype

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.taxonomies.pages.TaxonomyArchivePage
import com.eden.orchid.taxonomies.pages.TermArchivePage
import javax.inject.Inject

class ArchiveIndexPathType
@Inject
constructor() : PermalinkPathType(100) {

    override fun acceptsKey(page: OrchidPage, key: String): Boolean {
        return key == "archiveIndex" && (page is TaxonomyArchivePage || page is TermArchivePage)
    }

    override fun format(page: OrchidPage, key: String): String? {
        val index = if (page is TaxonomyArchivePage) page.index
        else if (page is TermArchivePage) page.index
        else 0

        return if (index == 0) null else if (index == 1) "" else "$index"
    }

}

