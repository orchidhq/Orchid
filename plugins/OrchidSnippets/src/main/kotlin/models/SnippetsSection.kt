package com.eden.orchid.snippets.models

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.ImpliedKey
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.snippets.adapter.SnippetsAdapter

class SnippetsSection : OptionsHolder {

    @Option
    @StringDefault("file")
    @Description("The configuration for the default snippets loader section.")
    lateinit var adapter: SnippetsAdapter

    @Option
    @Description("Tags to apply to each snippet in this section.")
    lateinit var tags: List<String>

    fun addSnippets(context: OrchidContext): Sequence<Snippet> {
        return adapter
            .addSnippets(context)
            .map { config ->
                Snippet(
                    name = config.name.takeIf { it.isNotEmpty() } ?: config.defaultName,
                    tags = config.defaultTags + config.tags + this.tags,
                    resource = config.resource
                )
            }
    }

}
