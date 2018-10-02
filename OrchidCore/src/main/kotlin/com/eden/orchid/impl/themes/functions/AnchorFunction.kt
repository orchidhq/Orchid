package com.eden.orchid.impl.themes.functions

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.indexing.IndexService
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage

import javax.inject.Inject
import java.util.ArrayList
import java.util.Collections

@Description(value = "Generate an HTML link to a page.", name = "Anchor")
class AnchorFunction @Inject
constructor(val context: OrchidContext) : TemplateFunction("anchor", true) {

    @Option
    @Description("The title to display in an anchor tag for the given item if found. Otherwise, the title is " + "returned directly.")
    var title: String? = null

    @Option
    @Description("The Id of an item to link to.")
    var itemId: String? = null

    @Option
    @Description("The type of collection the item is expected to come from.")
    var collectionType: String? = null

    @Option
    @Description("The specific Id of the given collection type where the item is expected to come from.")
    var collectionId: String? = null

    @Option
    @Description("Custom classes to add to the resulting anchor tag. If no matching item is found, these classes are " + "not used.")
    var customClasses: String? = null

    override fun parameters(): Array<String?> {
        val params = ArrayList<String>()
        params.add("title")
        Collections.addAll(params, *IndexService.locateParams)
        params.add("customClasses")
        val paramsArray = arrayOfNulls<String>(params.size)
        params.toTypedArray()
        return paramsArray
    }

    override fun apply(): String? {
        if (EdenUtils.isEmpty(itemId) && !EdenUtils.isEmpty(title)) {
            itemId = title
        }

        val page = context.findPage(collectionType, collectionId, itemId)

        if (page != null) {
            val link = page.link

            return if (!EdenUtils.isEmpty(title) && !EdenUtils.isEmpty(customClasses)) {
                Clog.format("<a href=\"#{$1}\" class=\"#{$3}\">#{$2}</a>", link, title, customClasses)
            } else if (!EdenUtils.isEmpty(title)) {
                Clog.format("<a href=\"#{$1}\">#{$2}</a>", link, title)
            } else {
                Clog.format("<a href=\"#{$1}\">#{$1}</a>", link)
            }
        }

        return if (!EdenUtils.isEmpty(title)) {
            title
        } else {
            ""
        }
    }
}
