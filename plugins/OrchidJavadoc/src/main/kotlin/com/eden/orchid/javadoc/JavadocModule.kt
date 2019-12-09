package com.eden.orchid.javadoc

import com.copperleaf.kodiak.java.JavadocInvokerImpl
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OrchidFlags
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.javadoc.menu.AllClassesMenuItemType
import com.eden.orchid.javadoc.menu.AllPackagesMenuItemType
import com.eden.orchid.javadoc.menu.ClassDocLinksMenuItemType
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.addToSet
import com.google.inject.Provides

class JavadocModule : OrchidModule() {

    override fun configure() {
        if(OrchidFlags.getInstance().getFlagValue<Boolean?>("experimentalSourceDoc") == true) {
            addToSet<OrchidGenerator<*>, NewJavadocGenerator>()
        }
        else {
            withResources(10)
            addToSet<OrchidGenerator<*>, JavadocGenerator>()
            addToSet<OrchidMenuFactory>(
                AllClassesMenuItemType::class,
                AllPackagesMenuItemType::class,
                ClassDocLinksMenuItemType::class)
        }
    }

    @Provides
    fun providesJavadocInvokerImpl(): JavadocInvokerImpl {
        return JavadocInvokerImpl(
            cacheDir = OrchidUtils.getCacheDir("sourcedoc")
        )
    }

}
