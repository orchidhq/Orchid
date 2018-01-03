package com.eden.orchid.changelog

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.changelog.components.ChangelogComponent
import com.eden.orchid.changelog.components.ChangelogVersionPicker

class ChangelogModule : OrchidModule() {

    override fun configure() {
        addToSet(PluginResourceSource::class.java,
                ChangelogResourceSource::class.java)

        addToSet(OrchidGenerator::class.java,
                ChangelogGenerator::class.java)

        addToSet(OrchidComponent::class.java,
                ChangelogComponent::class.java,
                ChangelogVersionPicker::class.java)
    }
}

