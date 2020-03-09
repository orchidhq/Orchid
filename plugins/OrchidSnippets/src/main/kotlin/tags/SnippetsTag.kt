package com.eden.orchid.snippets.tags

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.snippets.models.Snippet
import com.eden.orchid.snippets.models.SnippetsModel
import com.eden.orchid.utilities.resolve

class SnippetsTag : TemplateTag("snippets", Type.Simple, true), SnippetsModel.SnippetsQuery {

    @Option
    @Description("the snippet tags")
    override lateinit var snippetTags: List<String>

    @Option
    @Description("The ID to add to tabs. Defaults to the tags used to query snippets.")
    override var id: String = ""
        get() {
            return field.takeIf { it.isNotBlank() } ?: snippetTags.sorted().joinToString("_")
        }

    lateinit var snippets: List<Snippet>

    override fun parameters() = arrayOf(::snippetTags.name, ::id.name)

    override fun onRender(context: OrchidContext, page: OrchidPage?) {
        super.onRender(context, page)

        val model = context.resolve<SnippetsModel>()
        snippets = model.getSnippets(this)
    }
}
