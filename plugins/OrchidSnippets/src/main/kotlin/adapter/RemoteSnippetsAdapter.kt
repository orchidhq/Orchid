package com.eden.orchid.snippets.adapter

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.snippets.models.SnippetConfig

@Description("Download a remote file over HTTP and locate snippets on that page either through regex or a CSS selector")
class RemoteSnippetsAdapter : SnippetsAdapter {

    override fun getType(): String = "remote"

    override fun addSnippets(context: OrchidContext): Sequence<SnippetConfig> = sequence {

    }
}
