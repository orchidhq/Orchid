package com.eden.orchid.search.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.components.OrchidComponent

@Description(
    "Adds the self-contained Lunr.js-based Orchid search component to your page.",
    name = "Algolia DocSearch"
)
class OrchidSearchComponent : OrchidComponent("orchidSearch", true) {

    override fun loadAssets() {
        addCss("orchidSearch.scss")

        addJs("https://unpkg.com/lunr/lunr.js")
        addJs("assets/js/orchidSearch.js")
    }

    override fun isHidden(): Boolean {
        return true
    }
}
