package com.eden.orchid.taxonomies.collections

import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.taxonomies.TaxonomiesGenerator
import com.eden.orchid.taxonomies.models.TaxonomiesModel
import com.eden.orchid.taxonomies.models.Taxonomy

@Description(
    "A Taxonomy Collection represents all the pages that have a given term value. The 'itemId' matches a " +
            "specific term, while the 'collectionId' represents the taxonomy the term is from."
)
open class TaxonomyLandingPagesCollection(
    generator: TaxonomiesGenerator,
    val model: TaxonomiesModel
) : OrchidCollection<OrchidPage>(
    generator,
    "taxonomies",
    {
        model
            .taxonomies
            .values
            .map { term: Taxonomy -> term.landingPage }
            .stream()
    }
)
