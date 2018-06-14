package com.eden.orchid.bsdoc

import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.utilities.addToSet

class BsDocModule : OrchidModule() {

    override fun configure() {
        addToSet<Theme, BSDocTheme>()
    }
}
