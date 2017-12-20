package com.eden.orchid.languages.highlighter

import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.components.OrchidComponent
import org.jtwig.functions.JtwigFunction

class SyntaxHighlighterModule : OrchidModule() {

    override fun configure() {
        addToSet(PluginResourceSource::class.java,
                HighlightResourceSource::class.java)

        // Syntax highlighting at built-time via Pygments
        addToSet(JtwigFunction::class.java,
                HighlightFilter::class.java,
                HighlightPrismFilter::class.java)

        // Syntax Highlighting at runtime via Prism
        addToSet(OrchidComponent::class.java,
                PrismComponent::class.java)
    }
}
