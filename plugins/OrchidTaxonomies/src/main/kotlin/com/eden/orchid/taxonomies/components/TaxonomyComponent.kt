package com.eden.orchid.taxonomies.components

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.taxonomies.models.TaxonomiesModel
import com.eden.orchid.taxonomies.models.Taxonomy
import javax.inject.Inject

@Description("Show a list of all Taxonomies.", name = "Taxonomy")
class TaxonomyComponent
@Inject
constructor(
        context: OrchidContext,
        var model: TaxonomiesModel
) : OrchidComponent(context, "taxonomy", 100) {

    @Option
    @Description("The Taxonomy to include terms from.")
    lateinit var taxonomyType: String

    val taxonomy: Taxonomy?
        get() {
            return model.taxonomies[taxonomyType]
        }
}

