package com.eden.orchid.snippets.components

import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.snippets.models.Snippet
import com.eden.orchid.snippets.models.SnippetsModel
import com.eden.orchid.utilities.resolve

@Description(
    "Display in tabs the contents of a multiple snippets by tag.",
    name = "Snippet Tabs"
)
class SnippetsComponent : OrchidComponent("snippets", true), SnippetsModel.SnippetsQuery {

    @Option
    @Description("the snippet tags")
    override lateinit var snippetTags: List<String>

    @Option
    @Description("The ID to add to tabs. Defaults to the tags used to query snippets.")
    override var id: String = ""
        get() {
            return field.takeIf { it.isNotBlank() } ?: snippetTags.sorted().joinToString("_")
        }

    @Option
    @Description("render the raw snippet content without compiling it first")
    @BooleanDefault(false)
    var raw: Boolean = false

    val snippets: List<Snippet> by lazy {
        val model = context.resolve<SnippetsModel>()
        model.getSnippets(this)
    }
}
