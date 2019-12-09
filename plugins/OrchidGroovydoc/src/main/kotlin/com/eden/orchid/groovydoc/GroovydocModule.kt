package com.eden.orchid.groovydoc

import com.copperleaf.kodiak.groovy.GroovydocInvokerImpl
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OrchidFlags
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.groovydoc.menu.AllClassesMenuItemType
import com.eden.orchid.groovydoc.menu.AllPackagesMenuItemType
import com.eden.orchid.groovydoc.menu.ClassDocLinksMenuItemType
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.addToSet
import com.google.inject.Provides

class GroovydocModule() : OrchidModule() {

    override fun configure() {
        if(OrchidFlags.getInstance().getFlagValue<Boolean?>("experimentalSourceDoc") == true) {
            addToSet<OrchidGenerator<*>, NewGroovydocGenerator>()
        }
        else {
            withResources(10)
            addToSet<OrchidGenerator<*>, GroovydocGenerator>()
            addToSet<OrchidMenuFactory>(
                AllClassesMenuItemType::class,
                AllPackagesMenuItemType::class,
                ClassDocLinksMenuItemType::class)
        }
    }

    @Provides
    fun providesJavadocInvokerImpl(): GroovydocInvokerImpl {
        return GroovydocInvokerImpl(
            cacheDir = OrchidUtils.getCacheDir("sourcedoc")
        )
    }

}
