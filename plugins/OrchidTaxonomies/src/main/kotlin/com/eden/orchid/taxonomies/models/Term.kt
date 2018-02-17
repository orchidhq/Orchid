package com.eden.orchid.taxonomies.models

import com.eden.common.json.JSONElement
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Description
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

    @Option @IntDefault(100)
    @Description("The maximum number of associated pages to include in a single page in the Term archive.")
    var pageSize: Int = 100

    @Option @StringDefault(":taxonomy/:term/:archiveIndex")
    @Description("The permalink structure to use for this term's archive pages.")
    lateinit var permalink: String

    @Option
    @Description("A list of properties to order the associated pages by.")
    lateinit var orderBy: Array<String>

    @Option @StringDefault("desc")
    @Description("Whether to sort in ascending or descending order. One of [asc, desc].")
    lateinit var orderByDirection: String

    @Option
    @Description("The displayed title of the Term. Defaults to the un-camelCased Term key.")
    var title: String = ""
        get() {
            return if(!EdenUtils.isEmpty(field)) field else key.from { camelCase() }.to { titleCase() }
        }

    val landingPage: OrchidPage
        get() {
            return archivePages.first()
        }

    val link: String
        get() {
            return landingPage.link
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
