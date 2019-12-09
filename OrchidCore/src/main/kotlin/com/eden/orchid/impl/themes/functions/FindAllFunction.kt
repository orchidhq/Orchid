package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.indexing.IndexService
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage

@Description(value = "Get all Page objects matching a query.", name = "Find all")
class FindAllFunction : TemplateFunction("findAll", false) {

    @Option
    @Description("The Id of the items to link to.")
    lateinit var itemId: String

    @Option
    @Description("The type of collection the items are expected to come from.")
    lateinit var collectionType: String

    @Option
    @Description("The specific Id of the given collection type where the items are expected to come from.")
    lateinit var collectionId: String

    @Option
    @IntDefault(0)
    @Description("Paginate results starting at this page. Index starts at 1.")
    var pageIndex: Int = 0

    @Option
    @IntDefault(0)
    @Description("Paginate results using this as a page size")
    var pageSize: Int = 0

    override fun parameters(): Array<String> {
        return arrayOf(
                *IndexService.locateParams,
                "page",
                "pageSize",
                "pageIndex"
        )
    }

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        return if (pageIndex > 0 && pageSize > 0) {
            context.findAll(collectionType, collectionId, itemId, pageIndex, pageSize)
        } else {
            context.findAll(collectionType, collectionId, itemId)
        }
    }
}
