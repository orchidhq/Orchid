package com.eden.orchid.taxonomies.collections

import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.taxonomies.models.Taxonomy
import com.eden.orchid.taxonomies.models.Term
import java.util.stream.Stream

open class TaxonomyCollection(
        generator: OrchidGenerator,
        val taxonomy: Taxonomy)
: OrchidCollection<OrchidPage>("taxonomy", taxonomy.key, ArrayList()) {

    override fun find(id: String): Stream<OrchidPage> {
        return taxonomy
                .terms
                .values
                .stream()
                .filter { term: Term -> term.key == id }
                .map { term: Term -> term.landingPage }
    }

}
