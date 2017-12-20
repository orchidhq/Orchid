package com.eden.orchid.bsdoc

import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.Theme

class BsDocModule : OrchidModule() {

    override fun configure() {
        addToSet(Theme::class.java, BSDocTheme::class.java)
    }
}
