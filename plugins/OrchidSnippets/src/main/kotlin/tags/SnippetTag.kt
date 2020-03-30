package com.eden.orchid.snippets.tags

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.snippets.models.Snippet
import com.eden.orchid.snippets.models.SnippetsModel
import com.eden.orchid.utilities.resolve

class SnippetTag : TemplateTag("snippet", Type.Simple, true), SnippetsModel.SnippetQuery {

    @Option
    @Description("the snippet name")
    override lateinit var snippetName: String

    var snippet: Snippet? = null

    override fun parameters() = arrayOf(::snippetName.name)

    override fun onRender(context: OrchidContext, page: OrchidPage?) {
        super.onRender(context, page)

        val model = context.resolve<SnippetsModel>()
        snippet = model.getSnippet(this)
    }
}
