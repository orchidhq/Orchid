package com.eden.orchid.search.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.assets.AssetManagerDelegate
import com.eden.orchid.api.theme.components.OrchidComponent

@Description(
    "Adds the self-contained Lunr.js-based Orchid search component to your page. Requires search indices to be " +
            "generated.",
    name = "Orchid Static Search"
)
class OrchidSearchComponent : OrchidComponent("orchidSearch", true) {

    override fun loadAssets(delegate: AssetManagerDelegate) {
        delegate.addCss("assets/css/orchidSearch.scss")

        delegate.addJs("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js")
        delegate.addJs("https://unpkg.com/lunr/lunr.js")
        delegate.addJs("assets/js/orchidSearch.js")
    }

    override fun isHidden(): Boolean {
        return true
    }
}
