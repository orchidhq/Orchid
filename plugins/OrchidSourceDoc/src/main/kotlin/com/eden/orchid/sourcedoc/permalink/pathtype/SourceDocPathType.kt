package com.eden.orchid.sourcedoc.permalink.pathtype

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.sourcedoc.page.SourceDocPage
import javax.inject.Inject

class SourceDocPathType
@Inject
constructor() : PermalinkPathType(100) {

    override fun acceptsKey(page: OrchidPage, key: String): Boolean {
        return key == "sourceDocPath" && page is SourceDocPage<*>
    }

    override fun format(page: OrchidPage, key: String): String? {
        if (page is SourceDocPage<*>) {
            return page.element.id.replace('.', '/')
        }

        return null
    }

}

