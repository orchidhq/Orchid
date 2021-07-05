package com.eden.orchid.taxonomies.permalink.pathtype

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.taxonomies.pages.TermArchivePage
import javax.inject.Inject

class TermPathType
@Inject
constructor() : PermalinkPathType() {

    override fun acceptsKey(page: OrchidPage, key: String): Boolean {
        return key == "term" && page is TermArchivePage
    }

    override fun format(page: OrchidPage, key: String): String? {
        return (page as TermArchivePage).term.key
    }
}
