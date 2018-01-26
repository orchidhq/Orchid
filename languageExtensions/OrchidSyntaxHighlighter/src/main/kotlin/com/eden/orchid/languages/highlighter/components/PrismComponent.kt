package com.eden.orchid.languages.highlighter.components

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
    @StringDefault("https://cdnjs.cloudflare.com/ajax/libs/prism/1.8.4")
    lateinit var prismSource: String

    @Option
    lateinit var languages: Array<String>

    @Option
    lateinit var plugins: Array<String>

    @Option
    lateinit var theme: String

    @Option
    lateinit var githubTheme: String

    @Option
    var isScriptsOnly: Boolean = false

    override fun loadAssets() {
        addJs(OrchidUtils.normalizePath(prismSource) + "/prism.min.js")

        if (!isScriptsOnly) {
            if (!EdenUtils.isEmpty(theme)) {
                addCss("${OrchidUtils.normalizePath(prismSource)}/themes/prism-$theme.min.css")
            } else if (!EdenUtils.isEmpty(githubTheme)) {
                addCss("https://rawgit.com/PrismJS/prism-themes/master/themes/prism-$githubTheme.css")
            } else {
                addCss("${OrchidUtils.normalizePath(prismSource)}/themes/prism.min.css")
            }
        }

        if (!EdenUtils.isEmpty(languages)) {
            for (lang in languages) {
                addJs("${OrchidUtils.normalizePath(prismSource)}/components/prism-$lang.min.js")
            }
        }

        if (!EdenUtils.isEmpty(plugins)) {
            for (plugin in plugins) {
                if(plugin == "copy-to-clipboard") {
                    addJs("https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/1.7.1/clipboard.min.js")
                }
                addJs("${OrchidUtils.normalizePath(prismSource)}/plugins/$plugin/prism-$plugin.min.js")

                if (!isScriptsOnly) {
                    when (plugin) {
                        "line-numbers", "line-highlight", "toolbar" -> addCss("${OrchidUtils.normalizePath(prismSource)}/plugins/$plugin/prism-$plugin.min.css")
                    }
                }
            }
        }
    }

    override fun isHidden(): Boolean {
        return true
    }
}
