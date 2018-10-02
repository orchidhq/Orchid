package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.indexing.IndexService
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option

import javax.inject.Inject
import java.util.ArrayList
import java.util.Arrays

@Description(value = "Get all Page objects matching a query.", name = "Find all")
class FindAllFunction @Inject
constructor(val context: OrchidContext) : TemplateFunction("findAll", false) {

    @Option
    @Description("The Id of the items to link to.")
    var itemId: String? = null

    @Option
    @Description("The type of collection the items are expected to come from.")
    var collectionType: String? = null

    @Option
    @Description("The specific Id of the given collection type where the items are expected to come from.")
    var collectionId: String? = null

    @Option("page")
    @IntDefault(0)
    @Description("Paginate results starting at this page.")
    var pageIndex: Int = 0

    @Option
    @IntDefault(0)
    @Description("Paginate results using this as a page size")
    var pageSize: Int = 0

    override fun parameters(): Array<String> {
        val params = ArrayList(Arrays.asList(*IndexService.locateParams))
        params.add("page")
        params.add("pageSize")
        return params.toTypedArray()
    }

    override fun apply(): Any {
        return if (pageIndex > 0 && pageSize > 0) {
            context.findAll(collectionType, collectionId, itemId, pageIndex, pageSize)
        } else {
            context.findAll(collectionType, collectionId, itemId)
        }
    }
}
