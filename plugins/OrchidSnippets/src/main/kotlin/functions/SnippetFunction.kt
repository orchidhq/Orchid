package com.eden.orchid.snippets.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.snippets.models.SnippetsModel
import com.eden.orchid.utilities.resolve

class SnippetFunction : TemplateFunction("snippet", false), SnippetsModel.SnippetQuery {

    @Option
    @Description("the snippet name")
    override lateinit var snippetName: String

    override fun parameters() = arrayOf(::snippetName.name)

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        val model = context.resolve<SnippetsModel>()
        return model.getSnippet(this)
    }
}
