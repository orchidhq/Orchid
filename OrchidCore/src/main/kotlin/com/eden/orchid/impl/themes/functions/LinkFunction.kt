package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.indexing.IndexService
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage

import javax.inject.Inject

@Description(value = "Get the link to a page.", name = "Link")
class LinkFunction @Inject
constructor(val context: OrchidContext) : TemplateFunction("link", true) {

    @Option
    @Description("The Id of an item to link to.")
    var itemId: String? = null

    @Option
    @Description("The type of collection the item is expected to come from.")
    var collectionType: String? = null

    @Option
    @Description("The specific Id of the given collection type where the item is expected to come from.")
    var collectionId: String? = null

    override fun parameters(): Array<String> {
        return IndexService.locateParams
    }

    override fun apply(): Any {
        val page = context.findPage(collectionType, collectionId, itemId)
        return if (page != null) {
            page.link
        } else ""

    }
}
