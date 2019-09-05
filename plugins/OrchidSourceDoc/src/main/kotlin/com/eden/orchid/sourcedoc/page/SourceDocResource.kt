package com.eden.orchid.sourcedoc.page

import com.copperleaf.kodiak.common.DocElement
import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.FreeableResource
import com.eden.orchid.api.theme.pages.OrchidReference
import org.json.JSONObject

class SourceDocResource<T : DocElement>(
    context: OrchidContext,
    val element: T
) : FreeableResource(OrchidReference(context,"")) {

    init {
        reference.extension = "md"
    }

    override fun loadContent() {
        if (rawContent == null) {
            rawContent = element.comment.components.joinToString(" ")
            content = rawContent
            embeddedData = JSONElement(JSONObject())
        }
    }

}
