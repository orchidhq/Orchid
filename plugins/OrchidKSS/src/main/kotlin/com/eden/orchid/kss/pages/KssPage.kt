package com.eden.orchid.kss.pages

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.kss.parser.StyleguideSection

class KssPage(
        context: OrchidContext, val styleguideSection: StyleguideSection)
    : OrchidPage(KssSectionResource(context, styleguideSection), "styleguide") {

    var parent: KssPage? = null
    var children: MutableList<KssPage> = ArrayList()

    var stylesheet: String? = null
        get() {
            if (!EdenUtils.isEmpty(styleguideSection.stylesheet))
                return styleguideSection.stylesheet
            else if (!EdenUtils.isEmpty(field))
                return field
            else
                return ""
        }

    override fun getTitle(): String {
        return "Section " + styleguideSection.styleGuideReference + " - " + styleguideSection.name
    }

    fun hasStylesheet(): Boolean {
        if (!EdenUtils.isEmpty(styleguideSection.stylesheet)) {
            return true
        } else if (!EdenUtils.isEmpty(stylesheet)) {
            return true
        }

        return false
    }

}
