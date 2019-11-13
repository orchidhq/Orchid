package com.eden.orchid.languages.ink.components

import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent

class InkStoryComponent : OrchidComponent("inkStory", false) {

    @Option
    lateinit var src: String

    override fun loadAssets() {
        val srcResource = context.getResourceEntry(src, null)
        val content = srcResource.compileContent(null)

        addCss("assets/css/inkle.scss")
        addJs("assets/js/inkle.js")
        addJs("inline:.js:var storyContent = $content").apply { inlined() }
        addJs("assets/js/inkleMain.js")
    }
}
