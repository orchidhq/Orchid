package com.eden.orchid.api.resources.resource

import com.eden.common.json.JSONElement
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.asInputStream
import com.eden.orchid.utilities.merge
import org.json.JSONObject
import java.io.InputStream

/**
 * A Resource type that provides a plain String as content to a template. When used with renderTemplate() or renderString(),
 * this resource will supply the `page.content` variable to the template renderer. When used with renderRaw(), the raw
 * plain String content will be written directly instead.
 */

class StringResource(
    reference: OrchidReference,
    private val hardcodedString: String,
    private val hardcodedData: JSONElement? = null
) : OrchidResource(reference) {

    override fun getContentStream(): InputStream {
        return hardcodedString.asInputStream()
    }

    override val embeddedData: JSONElement get() {
        return merge(hardcodedData, super.embeddedData) ?: JSONElement(JSONObject())
    }
}
