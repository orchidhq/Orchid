package com.eden.orchid.sourcedoc.permalink.pathtype

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.sourcedoc.page.BaseSourceDocPage
import javax.inject.Inject

class ModulePathType
@Inject
constructor() : PermalinkPathType(100) {

    override fun acceptsKey(page: OrchidPage, key: String): Boolean {
        return key == "module" && page is BaseSourceDocPage
    }

    override fun format(page: OrchidPage, key: String): String? {
        if (page is BaseSourceDocPage) {
            return page.module
        }

        return null
    }

}

