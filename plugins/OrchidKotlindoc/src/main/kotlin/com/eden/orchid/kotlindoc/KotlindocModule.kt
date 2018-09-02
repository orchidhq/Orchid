package com.eden.orchid.kotlindoc

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.kotlindoc.menu.AllClassesMenuItemType
import com.eden.orchid.kotlindoc.menu.AllPackagesMenuItemType
import com.eden.orchid.utilities.addToSet

class KotlindocModule : OrchidModule() {

    override fun configure() {
        withResources(10)

        addToSet<OrchidGenerator, KotlindocGenerator>()
        addToSet<OrchidMenuItem>(
                AllClassesMenuItemType::class,
                AllPackagesMenuItemType::class
        )
    }

}
