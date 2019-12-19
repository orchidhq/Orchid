package com.eden.orchid.languages.bible

import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.languages.bible.components.ReftaggerComponent
import com.eden.orchid.utilities.addToSet

class BibleModule : OrchidModule() {

    override fun configure() {
        withResources()

        addToSet<OrchidComponent, ReftaggerComponent>()
    }

}
