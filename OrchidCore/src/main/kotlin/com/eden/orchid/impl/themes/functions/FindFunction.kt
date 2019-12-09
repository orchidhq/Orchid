package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.indexing.IndexService
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage

@Description(value = "Lookup a Page object by a query.", name = "Find")
class FindFunction : TemplateFunction("find", false) {

    @Option
    @Description("The Id of an item to look up.")
    lateinit var itemId: String

    @Option
    @Description("The type of collection the item is expected to come from.")
    lateinit var collectionType: String

    @Option
    @Description("The specific Id of the given collection type where the item is expected to come from.")
    lateinit var collectionId: String

    override fun parameters(): Array<String> {
        return IndexService.locateParams
    }

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        return context.find(collectionType, collectionId, itemId)
    }
}
