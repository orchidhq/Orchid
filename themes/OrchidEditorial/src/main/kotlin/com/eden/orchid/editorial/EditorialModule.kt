package com.eden.orchid.editorial

import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.Theme

class EditorialModule : OrchidModule() {

    override fun configure() {
        addToSet(Theme::class.java, EditorialTheme::class.java)
    }
}
