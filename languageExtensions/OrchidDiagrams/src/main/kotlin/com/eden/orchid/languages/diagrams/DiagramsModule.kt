package com.eden.orchid.languages.diagrams

import com.eden.orchid.api.compilers.OrchidCompiler
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.utilities.addToSet

class DiagramsModule : OrchidModule() {

    override fun configure() {
        addToSet<OrchidCompiler, PlantUmlCompiler>()
    }
}
