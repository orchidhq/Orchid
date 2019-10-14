package com.eden.orchid.languages.highlighter.components

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.components.OrchidComponent
import javax.inject.Inject

@Description(
    "Add the Kotlin Playground to your pages, to convert Kotlin code snippets into interactive, embedded " +
            "development playgrounds.",
    name = "Kotlin Playground"
)
class KotlinPlaygroundComponent
@Inject
constructor(
    context: OrchidContext
) : OrchidComponent(context, "kotlinPlayground", 100) {

    @Option
    @Description("The base URL to load Kotlin Playground JS files from.")
    @StringDefault("https://unpkg.com/kotlin-playground@1")
    lateinit var kotlinPlaygroundSource: String

    @Option
    @Description("Select which elements on the page are converted. Defaults to markdown code blocks with the " +
            "`run-kotlin` language."
    )
    @StringDefault("pre code[class='language-run-kotlin']")
    lateinit var selector: String

    @Option
    @Description("The URL to a self-hosted server instance for running code snippets.")
    lateinit var server: String

    override fun loadAssets() {
        addJs(kotlinPlaygroundSource).apply {
            attrs["data-selector"] = selector

            if (server.isNotBlank()) {
                attrs["data-server"] = server
            }
        }
    }

    override fun isHidden(): Boolean {
        return true
    }
}
