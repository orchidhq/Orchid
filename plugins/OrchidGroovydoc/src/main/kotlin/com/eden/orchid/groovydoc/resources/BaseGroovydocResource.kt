package com.eden.orchid.groovydoc.resources

import com.copperleaf.groovydoc.json.models.GroovydocDocElement
import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.FreeableResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.asInputStream
import org.json.JSONObject
import java.io.InputStream

open class BaseGroovydocResource(
        context: OrchidContext,
        qualifiedName: String,
        var doc: GroovydocDocElement
) : FreeableResource(OrchidReference(context, qualifiedName.replace("\\.".toRegex(), "/") + ".html")) {

    init {
        reference.extension = "md"
    }

    override fun getContentStream(): InputStream {
        return doc.simpleComment.asInputStream()
    }
}
