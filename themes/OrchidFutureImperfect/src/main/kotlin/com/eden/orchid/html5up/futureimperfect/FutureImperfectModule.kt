package com.eden.orchid.html5up.futureimperfect

import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.utilities.addToSet

class FutureImperfectModule : OrchidModule() {

    override fun configure() {
        addToSet<Theme, FutureImperfectTheme>()
    }
}
