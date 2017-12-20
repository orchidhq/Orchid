package com.eden.orchid.languages.asciidoc

import com.eden.orchid.api.compilers.OrchidCompiler
import com.eden.orchid.api.registration.OrchidModule

class AsciidocModule : OrchidModule() {

    override fun configure() {
        // Compilers
        addToSet(OrchidCompiler::class.java,
                AsciiDoctorCompiler::class.java)
    }
}
