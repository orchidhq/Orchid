@file:Suppress(SuppressedWarnings.DEPRECATION)
package com.eden.orchid.groovydoc

import com.copperleaf.kodiak.groovy.GroovydocInvokerImpl
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.SuppressedWarnings
import com.eden.orchid.utilities.addToSet
import com.google.inject.Provides

class GroovydocModule() : OrchidModule() {

    override fun configure() {
        addToSet<OrchidGenerator<*>, NewGroovydocGenerator>()
    }

    @Provides
    fun providesJavadocInvokerImpl(): GroovydocInvokerImpl {
        return GroovydocInvokerImpl(
            cacheDir = OrchidUtils.getCacheDir("sourcedoc")
        )
    }

}
