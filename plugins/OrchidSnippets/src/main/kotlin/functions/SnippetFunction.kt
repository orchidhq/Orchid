package com.eden.orchid.snippets.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.snippets.models.Snippet
import com.eden.orchid.snippets.models.SnippetsModel
import com.eden.orchid.utilities.resolve

class SnippetFunction : TemplateFunction("snippet", false) {

    @Option
    @Description("the snippet name")
    lateinit var snippetName: String

    override fun parameters(): Array<String> {
        return arrayOf("snippetName")
    }

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        val model = context.resolve<SnippetsModel>()
        return model.getSnippet(snippetName)
    }
}
