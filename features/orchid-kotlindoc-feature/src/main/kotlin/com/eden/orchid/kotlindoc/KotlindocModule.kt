@file:Suppress(SuppressedWarnings.DEPRECATION)
package com.eden.orchid.kotlindoc

import com.copperleaf.kodiak.kotlin.KotlindocInvokerImpl
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.SuppressedWarnings
import com.eden.orchid.utilities.addToSet
import com.google.inject.Provides

class KotlindocModule : OrchidModule() {

    override fun configure() {
        addToSet<OrchidGenerator<*>, NewKotlindocGenerator>()
    }

    @Provides
    fun providesJavadocInvokerImpl(): KotlindocInvokerImpl {
        return KotlindocInvokerImpl(
            cacheDir = OrchidUtils.getCacheDir("sourcedoc")
        )
    }
}
