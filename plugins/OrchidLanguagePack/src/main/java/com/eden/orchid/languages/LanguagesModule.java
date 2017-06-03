package com.eden.orchid.languages;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.languages.impl.AsciiDoctorCompiler;
import com.eden.orchid.languages.impl.CSVParser;
import com.eden.orchid.languages.impl.TOMLParser;
import com.eden.orchid.languages.impl.TextCompiler;

public class LanguagesModule extends OrchidModule {

    @Override
    protected void configure() {
        // Compilers
        addToSet(OrchidCompiler.class,
                AsciiDoctorCompiler.class,
                TextCompiler.class);

        // Parsers
        addToSet(OrchidParser.class,
                CSVParser.class,
                TOMLParser.class);
    }
}
