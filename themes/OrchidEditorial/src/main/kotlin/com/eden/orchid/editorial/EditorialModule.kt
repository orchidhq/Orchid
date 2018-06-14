package com.eden.orchid.editorial

import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.utilities.addToSet

class EditorialModule : OrchidModule() {

    override fun configure() {
        addToSet<Theme, EditorialTheme>()
    }
}
