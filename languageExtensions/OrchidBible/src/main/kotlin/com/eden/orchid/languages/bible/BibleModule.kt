package com.eden.orchid.languages.bible

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.languages.bible.functions.BibleVerseFunction
import com.eden.orchid.utilities.addToSet

class BibleModule : OrchidModule() {

    override fun configure() {
        addToSet<TemplateFunction, BibleVerseFunction>()
    }

}
