package com.eden.orchid.snippets.models

import com.eden.orchid.api.resources.resource.OrchidResource

data class Snippet(
    val name: String,
    val tags: List<String>,
    private val resource: OrchidResource
) {

    val content: String by lazy {
        resource.compileContent(null)
    }
}
