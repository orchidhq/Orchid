package com.eden.orchid.javadoc

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.javadoc.menu.AllClassesMenuItemType
import com.eden.orchid.javadoc.menu.AllPackagesMenuItemType
import com.eden.orchid.javadoc.menu.ClassDocLinksMenuItemType
import com.eden.orchid.utilities.addToSet

class JavadocModule() : OrchidModule() {

    override fun configure() {
        withResources(10)

        addToSet<OrchidGenerator<*>, JavadocGenerator>()
        addToSet<OrchidMenuFactory>(
                AllClassesMenuItemType::class,
                AllPackagesMenuItemType::class,
                ClassDocLinksMenuItemType::class)
    }

}
