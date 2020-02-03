package com.eden.orchid.sourcedoc.page

import com.copperleaf.kodiak.common.DocElement
import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.FreeableResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.asInputStream
import org.json.JSONObject
import java.io.InputStream

class SourceDocResource<T : DocElement>(
    context: OrchidContext,
    val element: T
) : FreeableResource(OrchidReference(context,"")) {

    init {
        reference.extension = "md"
    }

    override fun getContentStream(): InputStream {
        return element.comment.components.joinToString(" ").asInputStream()
    }
}
