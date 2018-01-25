package com.eden.orchid.languages.highlighter

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.languages.highlighter.components.PrismComponent
import com.eden.orchid.languages.highlighter.tags.HighlightTag

class SyntaxHighlighterModule : OrchidModule() {

    override fun configure() {
        addToSet(PluginResourceSource::class.java,
                HighlightResourceSource::class.java)

        // Syntax Highlighting at build time via Pygments
        addToSet(TemplateTag::class.java,
                HighlightTag::class.java)

        // Syntax Highlighting at runtime via Prism
        addToSet(OrchidComponent::class.java,
                PrismComponent::class.java)
    }
}
