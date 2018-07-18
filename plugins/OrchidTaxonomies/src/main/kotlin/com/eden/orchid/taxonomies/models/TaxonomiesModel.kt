package com.eden.orchid.taxonomies.models

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.pages.OrchidPage
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaxonomiesModel

@Inject
constructor(val context: OrchidContext) {

    var taxonomies = HashMap<String, Taxonomy>()

    fun initialize() {
        this.taxonomies = HashMap()
    }

    fun onIndexingTermsFinished() {
        val keysToRemove = ArrayList<String>()
        for(key in taxonomies.keys) {
            if(taxonomies[key]!!.terms.isEmpty()) {
                keysToRemove.add(key)
            }
        }

        for(key in keysToRemove) {
            taxonomies.remove(key)
        }
    }

    fun getTaxonomy(taxonomy: String) : Taxonomy {
        return taxonomies[taxonomy]!!
    }

    fun putTaxonomy(taxonomy: Taxonomy) {
        taxonomies[taxonomy.key] = taxonomy
    }

    fun addPage(taxonomy: Taxonomy, term: String, page: OrchidPage) {
        val termOptions = taxonomy.query(term)

        taxonomy.addPage(term, page, (termOptions?.element as? JSONObject)?.toMap() ?: HashMap())
    }
}
