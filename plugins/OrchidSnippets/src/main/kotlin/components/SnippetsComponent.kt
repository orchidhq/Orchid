package com.eden.orchid.snippets.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.snippets.models.Snippet
import com.eden.orchid.snippets.models.SnippetsModel
import com.eden.orchid.utilities.resolve

class SnippetsComponent : OrchidComponent("snippets", true) {

    @Option
    @Description("the snippet tags")
    lateinit var snippetTags: List<String>

    val snippets: List<Snippet> by lazy {
        val model = context.resolve<SnippetsModel>()
        model.getSnippets(snippetTags)
    }
}
