package com.eden.orchid.groovydoc

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.groovydoc.menu.AllClassesMenuItemType
import com.eden.orchid.groovydoc.menu.AllPackagesMenuItemType
import com.eden.orchid.groovydoc.menu.ClassDocLinksMenuItemType
import com.eden.orchid.utilities.addToSet

class GroovydocModule() : OrchidModule() {

    override fun configure() {
        withResources(10)

        addToSet<OrchidGenerator<*>, GroovydocGenerator>()
        addToSet<OrchidMenuFactory>(
                AllClassesMenuItemType::class,
                AllPackagesMenuItemType::class,
                ClassDocLinksMenuItemType::class)
    }

}
