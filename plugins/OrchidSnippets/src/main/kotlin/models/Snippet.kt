package com.eden.orchid.snippets.models

import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage

data class Snippet(
    val name: String,
    val tags: List<String>,
    private val resource: OrchidResource
) {

    /**
     * Returns the default compiled content of this snippet, without any custom variables or anything else. It's the
     * snippet content exactly as written. The output is evaluated lazily and cached.
     */
    val content: String by lazy {
        resource.compileContent(resource.reference.context, null)
    }

    /**
     * Returns the snippet content in a form intended to be interpolated into the context of a page. As the snippet is
     * compiled against a specific set of data (a particular page), its output is not cached.
     */
    fun snippetContent(raw: Boolean, page: OrchidPage?): String {
        return if(raw) resource.content else resource.compileContent(page?.context ?: resource.reference.context, page)
    }
}
