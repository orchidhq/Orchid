package com.eden.orchid.impl.relations

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.Relation
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage

import javax.inject.Inject

class PageRelation
@Inject
constructor(
        context: OrchidContext
) : Relation<OrchidPage>(context) {

    @Option
    @Description("The Id of an item to look up.")
    lateinit var itemId: String

    @Option
    @Description("The type of collection the item is expected to come from.")
    lateinit var collectionType: String

    @Option
    @Description("The specific Id of the given collection type where the item is expected to come from.")
    lateinit var collectionId: String

    override fun load(): OrchidPage? {
        if(listOf(collectionType, collectionId, itemId).any { it.isNotBlank() }) {
            return context.findPage(this.collectionType, this.collectionId, this.itemId)
        }
        else {
            return null
        }
    }

}
