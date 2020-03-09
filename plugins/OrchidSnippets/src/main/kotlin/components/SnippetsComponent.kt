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

    @Option
    @Description("The ID to add to tabs. Defaults to the tags used to query snippets.")
    var id: String = ""
        get() {
            return field.takeIf { it.isNotBlank() } ?: snippetTags.sorted().joinToString("_")
        }

    val snippets: List<Snippet> by lazy {
        val model = context.resolve<SnippetsModel>()
        model.getSnippets(snippetTags)
    }
}
