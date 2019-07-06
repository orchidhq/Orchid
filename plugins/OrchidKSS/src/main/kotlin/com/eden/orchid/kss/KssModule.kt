package com.eden.orchid.kss

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.kss.menu.StyleguidePagesMenuItemType
import com.eden.orchid.kss.menu.StyleguideSectionsMenuItemType
import com.eden.orchid.utilities.addToSet

class KssModule : OrchidModule() {

    override fun configure() {
        withResources(20)
        addToSet<OrchidGenerator<*>, KssGenerator>()
        addToSet<OrchidMenuFactory>(
                StyleguidePagesMenuItemType::class,
                StyleguideSectionsMenuItemType::class)
    }
}

