package com.eden.orchid.swiftdoc

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.swiftdoc.menu.SwiftdocMenuItem

class SwiftdocModule : OrchidModule() {

    override fun configure() {
        super.configure()

        addToSet(PluginResourceSource::class.java,
                SwiftdocResourceSource::class.java)

        addToSet(OrchidGenerator::class.java,
                SwiftdocGenerator::class.java)

        addToSet(OrchidMenuItem::class.java,
                SwiftdocMenuItem::class.java)
    }

}