package com.eden.orchid.languages.highlighter

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.languages.highlighter.components.PrismComponent
import com.eden.orchid.languages.highlighter.tags.HighlightTag
import com.eden.orchid.utilities.addToSet

class SyntaxHighlighterModule : OrchidModule() {

    override fun configure() {
        withResources(750)

        addToSet<TemplateTag, HighlightTag>()
        addToSet<OrchidComponent, PrismComponent>()
    }
}
