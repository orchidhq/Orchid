package com.eden.orchid.forms

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource

class SearchModule : OrchidModule() {

    override fun configure() {
        addToSet(PluginResourceSource::class.java,
                SearchResourceSource::class.java)

        addToSet(OrchidGenerator::class.java,
                SearchIndexGenerator::class.java)
    }

}
