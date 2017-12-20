package com.eden.orchid.materialize

import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.Theme

class MaterializeModule : OrchidModule() {

    override fun configure() {
        addToSet(Theme::class.java, MaterializeTheme::class.java)
    }
}
