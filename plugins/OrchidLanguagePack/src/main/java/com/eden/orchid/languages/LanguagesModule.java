package com.eden.orchid.languages;

import com.eden.americanbiblesociety.ABSRepository;
import com.eden.flexmark.BibleVerseExtension;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.languages.impl.AsciiDoctorCompiler;
import com.eden.orchid.languages.impl.CSVParser;
import com.eden.orchid.languages.impl.CodeFilter;
import com.eden.orchid.languages.impl.PrismComponent;
import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.jtwig.functions.JtwigFunction;

public class LanguagesModule extends OrchidModule {

    @Override
    protected void configure() {
        // Compilers
        addToSet(OrchidCompiler.class,
                AsciiDoctorCompiler.class);

        // Parsers
        addToSet(OrchidParser.class,
                CSVParser.class);

        // Syntax highlighting at built-time via Pygments
        addToSet(JtwigFunction.class,
                CodeFilter.class);

        // Syntax Highlighting at runtime via Prism
        addToSet(OrchidComponent.class,
                PrismComponent.class);

        // Bible verses in Markdown
        addToSet(Extension.class, BibleVerseExtension.create());
        MutableDataSet absOptions = new MutableDataSet();
        absOptions.set(BibleVerseExtension.BIBLE_REPOSITORY, ABSRepository.class);
        addToSet(MutableDataSet.class, absOptions);
    }
}
