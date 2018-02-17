package com.eden.orchid.languages.highlighter.components

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.utilities.OrchidUtils
import javax.inject.Inject

class PrismComponent @Inject
constructor(context: OrchidContext) : OrchidComponent(context, "prism", 100) {

    @Option @StringDefault("https://cdnjs.cloudflare.com/ajax/libs/prism/1.8.4")
    @Description("The base URL to load Prism CSS and JS files from.")
    lateinit var prismSource: String

    @Option
    @Description("The Prism language definitions to be included.")
    lateinit var languages: Array<String>

    @Option
    @Description("The Prism plugins to be included.")
    lateinit var plugins: Array<String>

    @Option
    @Description("The official Prism theme to be used. Alternatively, you may use a `githubTheme` to use one of the " +
            "themes provided from the 'PrismJS/prism-themes' github repo."
    )
    lateinit var theme: String

    @Option
    @Description("The unofficial Prism theme to be used, from the 'PrismJS/prism-themes' github repo.")
    lateinit var githubTheme: String

    @Option
    @Description("If true, only include the Prism Javascript files, opting to build the styles yourself.")
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
