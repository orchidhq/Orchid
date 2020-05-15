package com.eden.orchid.languages.diagrams.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.assets.AssetManagerDelegate
import com.eden.orchid.api.theme.components.OrchidComponent

@Description("Convert MermaidJS code snippets into diagrams in your browser.",
        name = "Mermaid"
)
class MermaidJsComponent : OrchidComponent("mermaid", true) {

    @Option
    @StringDefault("https://cdnjs.cloudflare.com/ajax/libs/mermaid/8.4.3/mermaid.min.js")
    @Description("The base URL to load Mermaid files from.")
    lateinit var mermaidSource: String

    @Option
    @Description("Select which elements on the page are converted. Defaults to markdown code blocks with the " +
            "`mermaid` language."
    )
    @StringDefault("pre code[class='language-mermaid']")
    lateinit var selector: String

    override fun loadAssets(delegate: AssetManagerDelegate) {
        delegate.addJs(mermaidSource)
        delegate.addJs("assets/js/mermaidSetup.js") { inlined = true }
    }

    override fun isHidden(): Boolean {
        return true
    }
}
