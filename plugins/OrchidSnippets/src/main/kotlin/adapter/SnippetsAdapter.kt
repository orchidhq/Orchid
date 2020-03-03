package com.eden.orchid.snippets.adapter

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.components.ModularType
import com.eden.orchid.snippets.models.SnippetConfig

interface SnippetsAdapter : ModularType {
    fun addSnippets(context: OrchidContext) : Sequence<SnippetConfig>
}
