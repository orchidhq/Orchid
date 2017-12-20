package com.eden.orchid.languages.highlighter

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.utilities.OrchidUtils

import javax.inject.Inject

class PrismComponent @Inject
constructor(context: OrchidContext) : OrchidComponent(context, "prism", 100) {

    @Option
    @StringDefault("https://cdnjs.cloudflare.com/ajax/libs/prism/1.8.1")
    var prismSource: String? = null

    @Option
    var languages: Array<String>? = null

    @Option
    var theme: String? = null

    @Option
    var githubTheme: String? = null

    @Option
    var isScriptsOnly: Boolean = false

    override fun addAssets() {
        super.addAssets()

        addJs(OrchidUtils.normalizePath(prismSource)!! + "/prism.min.js")

        if (!isScriptsOnly) {
            if (!EdenUtils.isEmpty(theme)) {
                addCss(OrchidUtils.normalizePath(prismSource) + "/themes/prism-" + theme + ".min.css")
            } else if (!EdenUtils.isEmpty(githubTheme)) {
                addCss("https://raw.githubusercontent.com/PrismJS/prism-themes/master/themes/prism-$githubTheme.css")
            } else {
                addCss(OrchidUtils.normalizePath(prismSource)!! + "/themes/prism.min.css")
            }
        }

        if (!EdenUtils.isEmpty(languages)) {
            for (lang in languages!!) {
                addJs(OrchidUtils.normalizePath(prismSource) + "/components/prism-" + lang + ".min.js")
            }
        }
    }

    override fun isHidden(): Boolean {
        return true
    }
}
