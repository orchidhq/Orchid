package com.eden.orchid.taxonomies.models

import com.eden.common.json.JSONElement
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.AllOptions
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.utilities.camelCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.titleCase
import com.eden.orchid.utilities.to
import org.json.JSONObject
import javax.inject.Inject

class Taxonomy
@Inject
constructor(
        val context: OrchidContext
) : OptionsHolder {

    val terms = HashMap<String, Term>()
    lateinit var archivePages: List<OrchidPage>

    @Option
    lateinit var key: String

    @AllOptions
    lateinit var allData: Map<String, Any>

    @Option
    @IntDefault(100)
    @Description("The maximum number of term pages to include in a single page in the Taxonomy archive.")
    var pageSize: Int = 100

    @Option
    @StringDefault(":taxonomy/:archiveIndex")
    @Description("The permalink structure to use for this taxonomy's archive pages.")
    lateinit var permalink: String

    @Option
    @Description("Whether a single page may be associated to multiple Terms in this Taxonomy, or just a single one.")
    var single: Boolean = false

    @Option
    @Description("If `single` is false, you may still use singular properties of a page to be included as that " +
            "page's only Term, by setting it as the `singleKey`. For example, 'categories' may not be a singular " +
            "Taxonomy, but a page may have a singular property of 'category' that should still be captured."
    )
    lateinit var singleKey: String

    @Option
    @Description("A list of properties to order the Terms by.")
    lateinit var orderBy: Array<String>

    @Option
    @StringDefault("desc")
    @Description("Whether to sort in ascending or descending order. One of [asc, desc].")
    lateinit var orderByDirection: String

    @Option
    @Description("A list of generator keys whose pages are considered in this taxonomy.")
    lateinit var includeFrom: Array<String>

    @Option
    @Description("A list of generator keys whose pages are ignored by this taxonomy.")
    lateinit var excludeFrom: Array<String>

    @Option
    @Description("The displayed title of the Taxonomy. Defaults to the un-camelCased Taxonomy key.")
    var title: String = ""
        get() {
            return if (!EdenUtils.isEmpty(field)) field else key.from { camelCase() }.to { titleCase() }
        }

    val link: String
        get() {
            return archivePages.first().link
        }

    var allTerms: List<Term> = ArrayList()
        private set
        get() {
            if (field.isEmpty() && terms.isNotEmpty()) {
                var sortedList = terms.values.toList()

                var comparator: Comparator<Term>? = null
                if (orderBy.size > 0) {
                    orderBy.forEach { prop ->
                        comparator = if (comparator == null)
                            compareBy { getTermValue(it, prop) }
                        else
                            comparator!!.thenBy { getTermValue(it, prop) }

                    }
                }
                else {
                    comparator = compareBy { it.title }
                }

                if (orderByDirection.equals("desc", ignoreCase = true)) {
                    comparator = comparator!!.reversed()
                }

                field = sortedList.sortedWith(comparator!!)
            }

            return field
        }

    fun getTerm(term: String, taxonomyOptions: Map<String, Any>): Term {
        if (!terms.containsKey(term)) {
            val newTerm = Term(term)
            newTerm.extractOptions(context, taxonomyOptions)
            terms[term] = newTerm
        }

        return terms[term]!!
    }

    public fun addPage(term: String, page: OrchidPage, termOptions: Map<String, Any>) {
        val termModel = getTerm(term, termOptions)
        termModel.pages.add(page)
    }

    private fun getTermValue(term: Term, key: String): Comparable<*> {
        return when (key) {
            "key"         -> term.key
            "title"       -> term.title
            "entryCount"  -> term.pages.size
            "newestEntry" -> term.pages.maxBy { it.publishDate }!!.publishDate
            "oldestEntry" -> term.pages.minBy { it.publishDate }!!.publishDate
            else          -> {
                if (term.element.has(key)) {
                    if (term.element.get(key) is String) {
                        term.element.getString(key)
                    }
                    else if (term.element.get(key) is Number) {
                        term.element.getNumber(key)
                    }
                    else if (term.element.get(key) is Boolean) {
                        term.element.getBoolean(key)
                    }
                }

                // else
                term.title
            }
        }
    }

    fun query(pointer: String): JSONElement? {
        return JSONElement(JSONObject(allData)).query(pointer)
    }

}
