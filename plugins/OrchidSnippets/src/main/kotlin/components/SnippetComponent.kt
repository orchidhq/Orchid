package com.eden.orchid.snippets.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.snippets.models.Snippet
import com.eden.orchid.snippets.models.SnippetsModel
import com.eden.orchid.utilities.resolve

class SnippetComponent : OrchidComponent("snippet", true), SnippetsModel.SnippetQuery {

    @Option
    @Description("the snippet name")
    override lateinit var snippetName: String

    val snippet: Snippet? by lazy {
        val model = context.resolve<SnippetsModel>()
        model.getSnippet(this)
    }
}
