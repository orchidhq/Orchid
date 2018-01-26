package com.eden.orchid.taxonomies.models

import com.eden.orchid.api.theme.pages.OrchidPage

class Taxonomy(val key: String) {

    val terms = HashMap<String, Term>()

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

}