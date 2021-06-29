package com.eden.orchid.api.resources.resource

import com.eden.common.util.EdenUtils.merge
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.asInputStream
import java.io.InputStream

/**
 * A Resource type that provides a plain String as content to a template. When used with renderTemplate() or
 * renderString(), this resource will supply the `page.content` variable to the template renderer. When used with
 * renderRaw(), the raw plain String content will be written directly instead.
 */

class StringResource
@JvmOverloads
constructor(
    reference: OrchidReference,
    private val hardcodedString: String,
    private val hardcodedData: Map<String, Any?> = emptyMap()
) : OrchidResource(reference) {

    override fun getContentStream(): InputStream {
        return hardcodedString.asInputStream()
    }

    override val embeddedData: Map<String, Any?>
        get() = merge(hardcodedData, super.embeddedData) ?: emptyMap()
}
