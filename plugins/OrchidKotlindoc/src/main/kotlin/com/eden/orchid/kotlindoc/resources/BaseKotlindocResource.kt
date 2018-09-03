package com.eden.orchid.kotlindoc.resources

import com.copperleaf.dokka.json.models.KotlinDocElement
import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.FreeableResource
import com.eden.orchid.api.theme.pages.OrchidReference
import org.json.JSONObject

open class BaseKotlindocResource(
        context: OrchidContext,
        qualifiedName: String,
        var doc: KotlinDocElement
) : FreeableResource(OrchidReference(context, qualifiedName.replace("\\.".toRegex(), "/") + ".html")) {

    init {
        reference.extension = "md"
    }

    override fun loadContent() {
        if (rawContent == null) {
            rawContent = doc.comment
            content = rawContent

            this.embeddedData = JSONElement(JSONObject())
        }
    }

}
