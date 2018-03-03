package com.eden.orchid.languages.bible

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.languages.bible.functions.BibleVerseFunction

class BibleModule : OrchidModule() {

    override fun configure() {
        addToSet(TemplateFunction::class.java,
                BibleVerseFunction::class.java)
    }

}
