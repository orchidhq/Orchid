package com.eden.orchid.snippets.models

import com.eden.orchid.api.resources.resource.OrchidResource

data class Snippet(
    val name: String,
    val tags: List<String>,
    private val resource: OrchidResource,
    private val offset: Int,
    private val length: Int?
) {

    val content: String by lazy {
        resource.compileContent(null).let {
            if (length == null) it.substring(offset)
            else it.substring(offset, offset + length)
        }
    }
}
