package com.eden.orchid.impl.themes.functions

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.indexing.IndexService
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage

@Description(value = "Generate an HTML link to a page.", name = "Anchor")
class AnchorFunction  : TemplateFunction("anchor", true) {

    @Option
    @Description("The title to display in an anchor tag for the given item if found. Otherwise, the title is " + "returned directly.")
    lateinit var title: String

    @Option
    @Description("The Id of an item to link to.")
    lateinit var itemId: String

    @Option
    @Description("The type of collection the item is expected to come from.")
    lateinit var collectionType: String

    @Option
    @Description("The specific Id of the given collection type where the item is expected to come from.")
    lateinit var collectionId: String

    @Option
    @Description("Custom classes to add to the resulting anchor tag. If no matching item is found, these classes are " + "not used.")
    lateinit var customClasses: String

    override fun parameters(): Array<String?> {
        return arrayOf(
                "title",
                *IndexService.locateParams,
                "customClasses"
        )
    }

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        if (EdenUtils.isEmpty(itemId) && !EdenUtils.isEmpty(title)) {
            itemId = title
        }

        return context.linkToPage(title, collectionType, collectionId, itemId, customClasses);
    }
}
