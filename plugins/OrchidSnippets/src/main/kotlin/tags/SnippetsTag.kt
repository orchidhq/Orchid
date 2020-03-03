package com.eden.orchid.snippets.tags

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.snippets.models.Snippet
import com.eden.orchid.snippets.models.SnippetsModel
import com.eden.orchid.utilities.resolve

class SnippetsTag : TemplateTag("snippets", Type.Simple, true) {

    @Option
    @Description("the snippet tags")
    lateinit var snippetTags: List<String>

    lateinit var snippets: List<Snippet>

    override fun parameters(): Array<String> {
        return arrayOf("snippetTags")
    }

    override fun onRender(context: OrchidContext, page: OrchidPage?) {
        super.onRender(context, page)

        val model = context.resolve<SnippetsModel>()
        snippets = model.getSnippets(snippetTags)
    }
}
