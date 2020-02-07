package com.eden.orchid.sourcedoc.page

import com.copperleaf.kodiak.common.DocElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.asInputStream
import java.io.InputStream

class SourceDocResource<T : DocElement>(
    context: OrchidContext,
    val element: T
) : OrchidResource(OrchidReference(context,"")) {

    init {
        reference.extension = "md"
    }

    override fun getContentStream(): InputStream {
        return element.comment.components.joinToString(" ").asInputStream()
    }
}
