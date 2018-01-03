package com.eden.orchid.kss.pages

import com.caseyjbrooks.clog.Clog
import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.FreeableResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.kss.parser.StyleguideSection
import com.eden.orchid.utilities.OrchidUtils
import org.json.JSONObject

class KssSectionResource(context: OrchidContext, val styleguideSection: StyleguideSection)
    : FreeableResource(OrchidReference(context, "styleguide/" + OrchidUtils.normalizePath(styleguideSection.styleGuidePath.joinToString("/")) + ".md")){

    override fun loadContent() {
        if (rawContent == null) {
            Clog.v("Loading KssSection content")
            rawContent = styleguideSection.description
            content = styleguideSection.description
            embeddedData = JSONElement(JSONObject(styleguideSection.tags))
        }
    }

}

