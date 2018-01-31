package com.eden.orchid.taxonomies.models

import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.OptionsData
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.utilities.camelCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.titleCase
import com.eden.orchid.utilities.to
import org.json.JSONObject

class Taxonomy(val context: OrchidContext, val key: String) : OptionsHolder {

    val terms = HashMap<String, Term>()
    lateinit var archivePages: List<OrchidPage>

    @OptionsData
    lateinit var allData: JSONElement

    @Option
    @IntDefault(100)
    var pageSize: Int = 100

    @Option @StringDefault(":taxonomy/:archiveIndex")
    lateinit var permalink: String

    @Option
    var single: Boolean = false

    @Option
    lateinit var singleKey: String

    @Option
    lateinit var orderBy: Array<String>

    @Option @StringDefault("desc")
    lateinit var orderByDirection: String

    @Option
    lateinit var includeFrom: Array<String>

    @Option
    lateinit var excludeFrom: Array<String>

    fun getTerm(term: String, taxonomyOptions: JSONObject) : Term {
        if(!terms.containsKey(term)) {
            val newTerm = Term(term)
            newTerm.extractOptions(context, taxonomyOptions)
            terms[term] = newTerm
        }

        return terms[term]!!
    }

    public fun addPage(term: String, page: OrchidPage, termOptions: JSONObject) {
        val termModel = getTerm(term, termOptions)
        termModel.pages.add(page)
    }

    val link: String
        get() {
            return archivePages.first().link
        }

    val title: String
        get() {
            return key.from { camelCase() }.to { titleCase() }
        }

    val allTerms: List<Term>
        get() {
            var sortedList = terms.values.toList()

            var comparator = compareBy<Term> { it.title }

            if(orderByDirection.equals("desc", ignoreCase = true)) {
                comparator = comparator.reversed()
            }

            return sortedList.sortedWith(comparator)
        }

}