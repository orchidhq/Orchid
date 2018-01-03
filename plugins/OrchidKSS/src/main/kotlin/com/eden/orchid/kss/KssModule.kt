package com.eden.orchid.kss

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.kss.menu.StyleguidePagesMenuItemType
import com.eden.orchid.kss.menu.StyleguideSectionsMenuItemType

class KssModule : OrchidModule() {

    override fun configure() {
        addToSet(OrchidGenerator::class.java,
                KssGenerator::class.java)

        addToSet(OrchidMenuItem::class.java,
                StyleguidePagesMenuItemType::class.java,
                StyleguideSectionsMenuItemType::class.java)

        addToSet(PluginResourceSource::class.java,
                KssResourceSource::class.java)
    }
}

