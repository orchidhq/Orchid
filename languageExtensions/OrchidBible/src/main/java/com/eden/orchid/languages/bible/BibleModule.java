package com.eden.orchid.languages.bible;

import com.eden.americanbiblesociety.ABSRepository;
import com.eden.flexmark.BibleVerseExtension;
import com.eden.orchid.api.registration.OrchidModule;
import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.util.options.MutableDataSet;

public class BibleModule extends OrchidModule {

    @Override
    protected void configure() {
        // Bible verses in Markdown
        addToSet(Extension.class, BibleVerseExtension.create());
        MutableDataSet absOptions = new MutableDataSet();
        absOptions.set(BibleVerseExtension.BIBLE_REPOSITORY, ABSRepository.class);
        addToSet(MutableDataSet.class, absOptions);
    }
}
