package com.eden.orchid.languages.bible

import com.eden.americanbiblesociety.ABSRepository
import com.eden.flexmark.BibleVerseExtension
import com.eden.orchid.api.registration.OrchidModule
import com.eden.repositories.EdenRepository
import com.vladsch.flexmark.Extension
import com.vladsch.flexmark.util.options.MutableDataSet

class BibleModule : OrchidModule() {

    override fun configure() {
        // Bible verses in Markdown
        addToSet(Extension::class.java, BibleVerseExtension.create())
        val absOptions = MutableDataSet()
        absOptions.set<Class<out EdenRepository>>(BibleVerseExtension.BIBLE_REPOSITORY, ABSRepository::class.java)
        addToSet(MutableDataSet::class.java, absOptions)
    }
}
