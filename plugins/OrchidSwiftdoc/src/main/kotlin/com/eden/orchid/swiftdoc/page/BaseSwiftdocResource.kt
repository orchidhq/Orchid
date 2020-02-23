package com.eden.orchid.swiftdoc.page

import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.swiftdoc.swift.SwiftStatement
import com.eden.orchid.utilities.asInputStream
import java.io.InputStream

open class BaseSwiftdocResource(
        reference: OrchidReference,
        var doc: SwiftStatement)
    : OrchidResource(reference) {

    init {
        reference.extension = "md"
    }

    override fun getContentStream(): InputStream {
        return  doc.data.optString("key.doc.comment").asInputStream()
    }

}
