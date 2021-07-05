@file:Suppress(SuppressedWarnings.DEPRECATION)
package com.eden.orchid.javadoc

import com.copperleaf.kodiak.java.JavadocInvokerImpl
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.SuppressedWarnings
import com.eden.orchid.utilities.addToSet
import com.google.inject.Provides

class JavadocModule : OrchidModule() {

    override fun configure() {
        addToSet<OrchidGenerator<*>, NewJavadocGenerator>()
    }

    @Provides
    fun providesJavadocInvokerImpl(): JavadocInvokerImpl {
        return JavadocInvokerImpl(
            cacheDir = OrchidUtils.getCacheDir("sourcedoc")
        )
    }
}
