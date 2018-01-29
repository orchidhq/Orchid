package com.eden.orchid.taxonomies.models

import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.utilities.camelCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.titleCase
import com.eden.orchid.utilities.to

class Term(val key: String) : OptionsHolder {

    public val pages = HashSet<OrchidPage>()
    lateinit var archivePages: List<OrchidPage>

    @Option
    @IntDefault(10)
    var pageSize: Int = 10

    @Option
    lateinit var permalink: String

    val link: String
        get() {
            return archivePages.first().link
        }

    val title: String
        get() {
            return key.from { camelCase() }.to { titleCase() }
        }

}