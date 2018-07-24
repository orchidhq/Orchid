package com.eden.orchid.javadoc

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.IgnoreModule
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.javadoc.menu.AllClassesMenuItemType
import com.eden.orchid.javadoc.menu.AllPackagesMenuItemType
import com.eden.orchid.javadoc.menu.ClassDocLinksMenuItemType
import com.eden.orchid.utilities.addToSet
import com.eden.orchid.utilities.bind
import com.sun.javadoc.RootDoc

@IgnoreModule
class JavadocModule(private val rootDoc: RootDoc) : OrchidModule() {

    override fun configure() {
        withResources(10)

        bind<RootDoc>().toInstance(rootDoc)

        addToSet<OrchidGenerator, JavadocGenerator>()
        addToSet<OrchidMenuItem>(
                AllClassesMenuItemType::class,
                AllPackagesMenuItemType::class,
                ClassDocLinksMenuItemType::class)
    }

}
