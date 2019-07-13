package com.eden.orchid.taxonomies.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.taxonomies.models.TaxonomiesModel
import com.eden.orchid.taxonomies.models.Taxonomy
import com.eden.orchid.utilities.resolve

@Description("Show a list of all Taxonomies.", name = "Taxonomy")
class TaxonomyComponent : OrchidComponent("taxonomy", 100) {

    @Option
    @Description("The Taxonomy to include terms from.")
    lateinit var taxonomyType: String

    val model: TaxonomiesModel by lazy {
        context.resolve<TaxonomiesModel>()
    }

    val taxonomy: Taxonomy?
        get() {
            return model.taxonomies[taxonomyType]
        }
}

