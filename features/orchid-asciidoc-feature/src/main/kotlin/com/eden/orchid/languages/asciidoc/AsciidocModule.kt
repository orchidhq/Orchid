package com.eden.orchid.languages.asciidoc

import com.eden.orchid.api.compilers.OrchidCompiler
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.utilities.addToSet

class AsciidocModule : OrchidModule() {

    override fun configure() {
        addToSet<OrchidCompiler, AsciiDoctorCompiler>()
    }
}
