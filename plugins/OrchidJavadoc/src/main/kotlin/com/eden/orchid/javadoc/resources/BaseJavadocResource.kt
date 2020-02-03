package com.eden.orchid.javadoc.resources

import com.copperleaf.javadoc.json.models.JavaDocElement
import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.FreeableResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.asInputStream
import java.io.InputStream

open class BaseJavadocResource(
        context: OrchidContext,
        qualifiedName: String,
        var doc: JavaDocElement
) : FreeableResource(OrchidReference(context, qualifiedName.replace("\\.".toRegex(), "/") + ".html")) {

    init {
        reference.extension = "md"
    }

    override fun getContentStream(): InputStream {
        return doc.simpleComment.asInputStream()
    }

}
