package com.eden.orchid.taxonomies

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule

class TaxonomiesModule : OrchidModule() {

    override fun configure() {
        addToSet(OrchidGenerator::class.java,
                TaxonomiesGenerator::class.java)
    }
}

