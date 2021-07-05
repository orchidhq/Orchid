package com.eden.orchid.snippets.models

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.utilities.extractOptionsFromResource

data class SnippetConfig(
    val context: OrchidContext,
    val defaultName: String,
    val defaultTags: List<String>,
    internal val resource: OrchidResource
) : OptionsHolder {

    init {
        extractOptionsFromResource(context, resource)
    }

    @Option
    lateinit var name: String

    @Option
    lateinit var tags: List<String>
}
