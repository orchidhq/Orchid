package com.eden.orchid.swiftdoc

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.swiftdoc.menu.SwiftdocMenuItem
import com.eden.orchid.utilities.addToSet

class SwiftdocModule : OrchidModule() {

    override fun configure() {
        withResources(10)

        addToSet<OrchidGenerator<*>, SwiftdocGenerator>()
        addToSet<OrchidMenuFactory, SwiftdocMenuItem>()
    }

}