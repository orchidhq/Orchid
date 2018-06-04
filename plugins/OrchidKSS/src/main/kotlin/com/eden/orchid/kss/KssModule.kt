package com.eden.orchid.kss

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.kss.menu.StyleguidePagesMenuItemType
import com.eden.orchid.kss.menu.StyleguideSectionsMenuItemType
import com.eden.orchid.utilities.addToSet

class KssModule : OrchidModule() {

    override fun configure() {
        addToSet<PluginResourceSource, KssResourceSource>()
        addToSet<OrchidGenerator, KssGenerator>()
        addToSet<OrchidMenuItem>(
                StyleguidePagesMenuItemType::class,
                StyleguideSectionsMenuItemType::class)
    }
}

