package com.eden.orchid.taxonomies.collections

import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.taxonomies.models.Taxonomy
import com.eden.orchid.taxonomies.models.Term

open class TaxonomyCollection(
        generator: OrchidGenerator,
        val taxonomy: Taxonomy)
: OrchidCollection<OrchidPage>(generator, "taxonomy", taxonomy.key, ArrayList()) {

    override fun find(id: String): List<OrchidPage> {
        return taxonomy
                .terms
                .values
                .filter { term: Term -> term.key == id }
                .map { term: Term -> term.landingPage }
    }

}
