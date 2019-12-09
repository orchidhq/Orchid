package com.eden.orchid.kotlindoc

import com.copperleaf.kodiak.kotlin.KotlindocInvokerImpl
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OrchidFlags
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.kotlindoc.menu.AllClassesMenuItemType
import com.eden.orchid.kotlindoc.menu.AllPackagesMenuItemType
import com.eden.orchid.kotlindoc.menu.KotlinClassDocLinksMenuItemType
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.addToSet
import com.google.inject.Provides

class KotlindocModule : OrchidModule() {

    override fun configure() {
        if(OrchidFlags.getInstance().getFlagValue<Boolean?>("experimentalSourceDoc") == true) {
            addToSet<OrchidGenerator<*>, NewKotlindocGenerator>()
        }
        else {
            withResources(10)
            addToSet<OrchidGenerator<*>, KotlindocGenerator>()
            addToSet<OrchidMenuFactory>(
                AllClassesMenuItemType::class,
                AllPackagesMenuItemType::class,
                KotlinClassDocLinksMenuItemType::class
            )
        }
    }

    @Provides
    fun providesJavadocInvokerImpl(): KotlindocInvokerImpl {
        return KotlindocInvokerImpl(
            cacheDir = OrchidUtils.getCacheDir("sourcedoc")
        )
    }

}
