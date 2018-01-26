package com.eden.orchid.taxonomies.models

import com.eden.orchid.api.theme.pages.OrchidPage
import javax.inject.Singleton

@Singleton
class TaxonomiesModel {

    lateinit var taxonomies: HashMap<String, Taxonomy>

    fun initialize() {
        this.taxonomies = HashMap<String, Taxonomy>()
    }

    public fun addPage(taxonomy: String, term: String, page: OrchidPage) {
        val taxonomyModel = if(!taxonomies.containsKey(taxonomy)) {
            val newTaxonomy = Taxonomy(taxonomy)
            taxonomies.put(taxonomy, newTaxonomy)
            newTaxonomy
        }
        else {
            taxonomies[taxonomy]!!
        }

        taxonomyModel.addPage(term, page)
    }
}