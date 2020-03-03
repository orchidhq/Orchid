package com.eden.orchid.snippets.adapter

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.snippets.models.SnippetConfig

@Description("Scan through files using regex to locate multiple snippets embedded inside each")
class EmbeddedSnippetsAdapter : SnippetsAdapter {

    override fun getType(): String = "embedded"

    override fun addSnippets(context: OrchidContext): Sequence<SnippetConfig> = sequence {

    }
}
