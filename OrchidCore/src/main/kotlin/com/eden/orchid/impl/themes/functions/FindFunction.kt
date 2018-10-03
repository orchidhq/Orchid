package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.indexing.IndexService
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option

import javax.inject.Inject

@Description(value = "Lookup a Page object by a query.", name = "Find")
class FindFunction @Inject
constructor(val context: OrchidContext) : TemplateFunction("find", false) {

    @Option
    @Description("The Id of an item to look up.")
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
        return context.find(collectionType, collectionId, itemId)
    }
}
