package com.eden.orchid.taxonomies.models

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.utilities.camelCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.titleCase
import com.eden.orchid.utilities.to

class Term(val key: String) {

    public val pages = HashSet<OrchidPage>()
    lateinit var archivePages: List<OrchidPage>

    val link: String
        get() {
            return archivePages.first().link
        }

    val title: String
        get() {
            return key.from { camelCase() }.to { titleCase() }
        }

}