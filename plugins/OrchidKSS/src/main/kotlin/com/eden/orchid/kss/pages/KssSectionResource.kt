package com.eden.orchid.kss.pages

import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.kss.parser.StyleguideSection
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.asInputStream
import com.eden.orchid.utilities.merge
import org.json.JSONObject
import java.io.InputStream

class KssSectionResource(
    context: OrchidContext,
    val styleguideSection: StyleguideSection
) : OrchidResource(
    OrchidReference(
        context,
        "styleguide/" + OrchidUtils.normalizePath(styleguideSection.styleGuidePath.joinToString("/")) + ".md"
    )
) {

    override fun getContentStream(): InputStream {
        return styleguideSection.description.asInputStream()
    }

    override val embeddedData: JSONElement
        get() =  merge(JSONElement(JSONObject(styleguideSection.tags)), super.embeddedData) ?: JSONElement(JSONObject())
}


