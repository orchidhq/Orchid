package com.eden.orchid.netlifycms

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource

class NetlifyCmsModule : OrchidModule() {

    override fun configure() {
        addToSet(PluginResourceSource::class.java,
                NetlifyCmsResourceSource::class.java)

        addToSet(OrchidGenerator::class.java,
                NetlifyCmsGenerator::class.java)
    }
}

