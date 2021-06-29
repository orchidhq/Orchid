@file:Suppress(SuppressedWarnings.DEPRECATION)
package com.eden.orchid.swiftdoc

import com.copperleaf.kodiak.swift.SwiftdocInvokerImpl
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.SuppressedWarnings
import com.eden.orchid.utilities.addToSet
import com.google.inject.Provides

class SwiftdocModule : OrchidModule() {

    override fun configure() {
        addToSet<OrchidGenerator<*>, NewSwiftdocGenerator>()
    }

    @Provides
    fun providesJavadocInvokerImpl(): SwiftdocInvokerImpl {
        return SwiftdocInvokerImpl(
            cacheDir = OrchidUtils.getCacheDir("sourcedoc")
        )
    }
}
