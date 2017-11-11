package com.eden.orchid.languages.highlighter;

import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.theme.components.OrchidComponent;
import org.jtwig.functions.JtwigFunction;

public class SyntaxHighlighterModule extends OrchidModule {

    @Override
    protected void configure() {
        // Syntax highlighting at built-time via Pygments
        addToSet(JtwigFunction.class,
                HighlightFilter.class);

        // Syntax Highlighting at runtime via Prism
        addToSet(OrchidComponent.class,
                PrismComponent.class);
    }
}
