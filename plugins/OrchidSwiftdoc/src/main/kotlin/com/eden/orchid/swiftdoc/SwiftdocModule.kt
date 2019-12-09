package com.eden.orchid.swiftdoc

import com.copperleaf.kodiak.swift.SwiftdocInvokerImpl
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OrchidFlags
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.swiftdoc.menu.SwiftdocMenuItem
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.addToSet
import com.google.inject.Provides

class SwiftdocModule : OrchidModule() {

    override fun configure() {
        if(OrchidFlags.getInstance().getFlagValue<Boolean?>("experimentalSourceDoc") == true) {
            addToSet<OrchidGenerator<*>, NewSwiftdocGenerator>()
        }
        else {
            withResources(10)
            addToSet<OrchidGenerator<*>, SwiftdocGenerator>()
            addToSet<OrchidMenuFactory, SwiftdocMenuItem>()
        }
    }

    @Provides
    fun providesJavadocInvokerImpl(): SwiftdocInvokerImpl {
        return SwiftdocInvokerImpl(
            cacheDir = OrchidUtils.getCacheDir("sourcedoc")
        )
    }

}
