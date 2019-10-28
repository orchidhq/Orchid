package com.eden.orchid.search.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent

@Description(
    "Add Algolia Docsearch to your site with minimal configuration.",
    name = "Algolia DocSearch"
)
class AlgoliaDocSearchComponent : OrchidComponent("algoliaDocsearch", true) {

    @Option
    @Description("The API key for your site.")
    lateinit var apiKey: String

    @Option
    @Description("The API key for your site.")
    lateinit var indexName: String

    @Option
    @Description("The selector for your search input field.")
    lateinit var selector: String

    @Option
    @Description("Set debug to true if you want to inspect the dropdown")
    var debug: Boolean = false

    override fun loadAssets() {
        addCss("https://cdn.jsdelivr.net/npm/docsearch.js@2/dist/cdn/docsearch.min.css")
        addJs("https://cdn.jsdelivr.net/npm/docsearch.js@2/dist/cdn/docsearch.min.js")
        addJs("assets/js/algoliaDocsearch.js").apply { inlined() }
    }

    override fun isHidden(): Boolean {
        return true
    }
}
