package com.eden.orchid.taxonomies.components

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.taxonomies.models.TaxonomiesModel
import com.eden.orchid.taxonomies.models.Taxonomy
import com.eden.orchid.taxonomies.models.Term
import javax.inject.Inject

class TaxonomyTermComponent @Inject
constructor(context: OrchidContext, var model: TaxonomiesModel) : OrchidComponent(context, "taxonomyTerm", 100) {

    @Option
    lateinit var taxonomyType: String

    @Option
    lateinit var termType: String

    val taxonomy: Taxonomy?
        get() {
            return model.taxonomies[taxonomyType]
        }

    val term: Term?
        get() {
            return taxonomy?.terms?.get(termType)
        }
}

