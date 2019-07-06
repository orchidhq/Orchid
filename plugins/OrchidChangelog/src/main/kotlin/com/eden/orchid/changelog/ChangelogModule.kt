package com.eden.orchid.changelog

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.publication.OrchidPublisher
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.changelog.components.ChangelogComponent
import com.eden.orchid.changelog.publication.RequiredChangelogVersionPublisher
import com.eden.orchid.utilities.addToSet

class ChangelogModule : OrchidModule() {

    override fun configure() {
        withResources(20)

        addToSet<OrchidGenerator<*>, ChangelogGenerator>()
        addToSet<OrchidPublisher, RequiredChangelogVersionPublisher>()
        addToSet<OrchidComponent, ChangelogComponent>()
    }

}

