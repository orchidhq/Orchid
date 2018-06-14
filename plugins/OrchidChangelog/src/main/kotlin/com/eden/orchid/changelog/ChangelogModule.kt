package com.eden.orchid.changelog

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.changelog.components.ChangelogComponent
import com.eden.orchid.changelog.components.ChangelogVersionPicker
import com.eden.orchid.utilities.addToSet

class ChangelogModule : OrchidModule() {

    override fun configure() {
        addToSet<PluginResourceSource, ChangelogResourceSource>()
        addToSet<OrchidGenerator, ChangelogGenerator>()
        addToSet<OrchidComponent>(
                ChangelogComponent::class,
                ChangelogVersionPicker::class)
    }
}

