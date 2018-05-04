package com.eden.orchid.swiftdoc.page

import com.eden.common.json.JSONElement
import com.eden.orchid.api.resources.resource.FreeableResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.swiftdoc.swift.SwiftStatement
import org.json.JSONObject

open class BaseSwiftdocResource(
        reference: OrchidReference,
        var doc: SwiftStatement)
    : FreeableResource(reference) {

    init {
        reference.extension = "md"
    }

    override fun loadContent() {
        if (rawContent == null) {
            rawContent = doc.data.optString("key.doc.comment")
            content = rawContent

            this.embeddedData = JSONElement(JSONObject())
        }
    }

}
