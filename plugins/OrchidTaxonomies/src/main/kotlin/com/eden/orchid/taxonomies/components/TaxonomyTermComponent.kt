package com.eden.orchid.taxonomies.components

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.taxonomies.models.TaxonomiesModel
import com.eden.orchid.taxonomies.models.Taxonomy
import com.eden.orchid.taxonomies.models.Term
import javax.inject.Inject

@Description("Show a list of all terms in a Taxonomy.", name = "Taxonomy Terms")
class TaxonomyTermComponent
@Inject
constructor(
        context: OrchidContext,
        var model: TaxonomiesModel
) : OrchidComponent(context, "taxonomyTerm", 100) {

    @Option
    @Description("The Taxonomy to include terms from.")
    lateinit var taxonomyType: String

    @Option
    @Description("The Term within the Taxonomy to include pages from.")
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

