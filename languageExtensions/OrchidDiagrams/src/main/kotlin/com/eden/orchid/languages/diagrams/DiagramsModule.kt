package com.eden.orchid.languages.diagrams

import com.eden.orchid.api.compilers.OrchidCompiler
import com.eden.orchid.api.registration.OrchidModule

class DiagramsModule : OrchidModule() {

    override fun configure() {
        // Compilers
        addToSet(OrchidCompiler::class.java,
                PlantUmlCompiler::class.java)
    }
}
