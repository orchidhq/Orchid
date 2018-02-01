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

    fun getTaxonomy(taxonomy: String, taxonomyOptions: JSONObject) : Taxonomy {
        if(!taxonomies.containsKey(taxonomy)) {
            val newTaxonomy = Taxonomy(context, taxonomy)
            newTaxonomy.extractOptions(context, taxonomyOptions)
            taxonomies[taxonomy] = newTaxonomy
        }

        return taxonomies[taxonomy]!!
    }

    fun addPage(taxonomy: String, term: String, page: OrchidPage) {
        val taxonomyModel = taxonomies[taxonomy]!!

        val termOptions = taxonomyModel.allData.query(term)

        taxonomyModel.addPage(term, page, termOptions?.element as? JSONObject ?: JSONObject())
    }
}
