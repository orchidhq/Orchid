package com.eden.orchid.editorial

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.api.theme.models.About
import com.eden.orchid.api.theme.models.Social

import javax.inject.Inject

class EditorialTheme @Inject
constructor(context: OrchidContext) : Theme(context, "Editorial", 100) {

    @Option
    @StringDefault("#f56a6a")
    var primaryColor: String? = null

    @Option
    var about: About? = null
    @Option
    var social: Social? = null

    override fun loadAssets() {
        addCss("assets/css/editorial_main.scss")
        addCss("assets/css/editorial_orchidCustomizations.scss")

        addJs("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js")
        addJs("https://cdnjs.cloudflare.com/ajax/libs/skel/3.0.1/skel.min.js")
        addJs("assets/js/editorial_util.js")
        addJs("assets/js/editorial_main.js")

        addJs("https://unpkg.com/lunr/lunr.js")
        addJs("assets/js/lunrSearch.js")
    }

}
