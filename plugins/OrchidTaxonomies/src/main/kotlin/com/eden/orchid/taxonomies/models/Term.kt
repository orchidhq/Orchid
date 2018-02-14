package com.eden.orchid.taxonomies.models

import com.eden.common.json.JSONElement
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.OptionsData
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.taxonomies.utils.getSingleTermValue
import com.eden.orchid.utilities.camelCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.titleCase
import com.eden.orchid.utilities.to

class Term(val key: String) : OptionsHolder {

    public var pages = HashSet<OrchidPage>()
    lateinit var archivePages: List<OrchidPage>

    @OptionsData
    lateinit var allData: JSONElement

    @Option
    @IntDefault(100)
    var pageSize: Int = 100

    @Option @StringDefault(":taxonomy/:term/:archiveIndex")
    lateinit var permalink: String

    @Option
    lateinit var orderBy: Array<String>

    @Option @StringDefault("desc")
    lateinit var orderByDirection: String

    val landingPage: OrchidPage
        get() {
            return archivePages.first()
        }

    val link: String
        get() {
            return landingPage.link
        }

    @Option
    var title: String = ""
        get() {
            return if(!EdenUtils.isEmpty(field)) field else key.from { camelCase() }.to { titleCase() }
        }

    var allPages: List<OrchidPage> = ArrayList()
        private set
        get() {
            if(field.isEmpty() && pages.isNotEmpty()) {
                var sortedList = pages.toList()

                var comparator: Comparator<OrchidPage>? = null
                if (orderBy.size > 0) {
                    orderBy.forEach { prop ->
                        comparator = if (comparator == null)
                            compareBy { it.getSingleTermValue(prop) }
                        else
                            comparator!!.thenBy { it.getSingleTermValue(prop) }

                    }
                } else {
                    comparator = compareBy<OrchidPage> { it.publishDate }.thenBy { it.title }
                }

                if (orderByDirection.equals("desc", ignoreCase = true)) {
                    comparator = comparator!!.reversed()
                }

                field = sortedList.sortedWith(comparator!!)
            }
            return field
        }

}
