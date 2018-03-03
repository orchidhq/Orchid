package com.eden.orchid.presentations

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OptionExtractor
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.presentations.components.PresentationComponent

class PresentationsModule : OrchidModule() {

    override fun configure() {
        addToSet(PluginResourceSource::class.java,
                PresentationsResourceSource::class.java)

        addToSet(OrchidGenerator::class.java,
                PresentationsGenerator::class.java)

        addToSet(OrchidComponent::class.java,
                PresentationComponent::class.java)

        addToSet(OptionExtractor::class.java,
                PresentationOptionExtractor::class.java)
    }

}
