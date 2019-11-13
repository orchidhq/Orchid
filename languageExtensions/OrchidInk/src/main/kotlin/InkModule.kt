package com.eden.orchid.languages.ink

import com.eden.orchid.api.compilers.OrchidCompiler
import com.eden.orchid.api.events.OrchidEventListener
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.languages.ink.components.InkStoryComponent
import com.eden.orchid.utilities.addToSet

class InkModule : OrchidModule() {

    override fun configure() {
        withResources()

        addToSet<OrchidCompiler, InkCompiler>()
        addToSet<OrchidEventListener, InkCompiler>()

        addToSet<OrchidComponent, InkStoryComponent>()
    }
}
