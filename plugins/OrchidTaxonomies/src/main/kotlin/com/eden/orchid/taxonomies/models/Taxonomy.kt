package com.eden.orchid.taxonomies.models

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.utilities.camelCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.titleCase
import com.eden.orchid.utilities.to

class Taxonomy(val key: String) {

    val terms = HashMap<String, Term>()
    lateinit var archivePages: List<OrchidPage>

    public fun addPage(term: String, page: OrchidPage) {
        val termModel = if(!terms.containsKey(term)) {
            val newTerm = Term(term)
            terms.put(term, newTerm)
            newTerm
        }
        else {
            terms[term]!!
        }

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

}