package com.eden.orchid.taxonomies.models

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage
import org.json.JSONObject

class TaxonomiesModel(
    val context: OrchidContext
) : OrchidGenerator.Model {

    override lateinit var allPages: List<OrchidPage>
    override var collections: List<OrchidCollection<*>> = emptyList()

    var taxonomies = HashMap<String, Taxonomy>()
    var collectionArchives = HashMap<Pair<String, String>, CollectionArchive>()

    fun onIndexingTermsFinished() {
        val keysToRemove = ArrayList<String>()
        for (key in taxonomies.keys) {
            if (taxonomies[key]!!.terms.isEmpty()) {
                keysToRemove.add(key)
            }
        }

        for (key in keysToRemove) {
            taxonomies.remove(key)
        }
    }

    fun getTaxonomy(taxonomy: String): Taxonomy {
        return taxonomies[taxonomy]!!
    }

    fun putTaxonomy(taxonomy: Taxonomy) {
        taxonomies[taxonomy.key] = taxonomy
    }

    fun putCollectionArchive(collectionArchive: CollectionArchive) {
        collectionArchives[collectionArchive.collectionType to collectionArchive.collectionId] = collectionArchive
    }

    fun addPage(taxonomy: Taxonomy, term: String, page: OrchidPage) {
        val termOptions = taxonomy.query(term)

        taxonomy.addPage(term, page, (termOptions?.element as? JSONObject)?.toMap() ?: HashMap())
    }
}
