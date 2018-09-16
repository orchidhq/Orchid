package com.eden.orchid.impl.relations

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.Relation
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.utilities.OrchidUtils
import javax.inject.Inject

class AssetRelation
@Inject
constructor(
        context: OrchidContext
) : Relation<String>(context) {

    @Option
    @Description("The filename and path of an asset to look up.")
    lateinit var itemId: String

    @Option
    @Description("The asset title.")
    lateinit var title: String

    @Option
    @Description("The asset alt text.")
    lateinit var alt: String

    val link: String
        get() = get()

    override fun load(): String {
        var fieldValue = this.itemId

        var shouldApplyBaseUrl = true

        if (!EdenUtils.isEmpty(fieldValue)) {
            if (OrchidUtils.isExternal(fieldValue)) {
                shouldApplyBaseUrl = false
            }
        }

        if (shouldApplyBaseUrl) {
            fieldValue = OrchidUtils.applyBaseUrl(context, fieldValue)
        }

        return fieldValue
    }

    override fun toString(): String {
        return get()
    }
}
