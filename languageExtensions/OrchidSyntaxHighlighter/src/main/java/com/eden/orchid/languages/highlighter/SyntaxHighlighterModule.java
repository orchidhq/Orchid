package com.eden.orchid.languages.highlighter;

import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.eden.orchid.api.theme.components.OrchidComponent;
import org.jtwig.functions.JtwigFunction;

public class SyntaxHighlighterModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(PluginResourceSource.class,
                HighlightResourceSource.class);

        // Syntax highlighting at built-time via Pygments
        addToSet(JtwigFunction.class,
                HighlightFilter.class,
                HighlightPrismFilter.class);

        // Syntax Highlighting at runtime via Prism
        addToSet(OrchidComponent.class,
                PrismComponent.class);
    }
}
