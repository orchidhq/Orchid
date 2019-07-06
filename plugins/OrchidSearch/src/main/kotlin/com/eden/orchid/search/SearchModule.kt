package com.eden.orchid.search

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.utilities.addToSet

class SearchModule : OrchidModule() {

    override fun configure() {
        withResources(20)
        addToSet<OrchidGenerator<*>, SearchIndexGenerator>()
    }

}
