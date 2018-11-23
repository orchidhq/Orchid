package com.eden.orchid.impl.relations

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.Relation
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.assets.AssetPage
import javax.inject.Inject

class AssetRelation
@Inject
constructor(
        context: OrchidContext
) : Relation<AssetPage>(context) {

    @Option
    @Description("The filename and path of an asset to look up.")
    lateinit var itemId: String

    val link: String get() = get()?.link ?: ""

    override fun load(): AssetPage? {
        return context.assetManager.createAsset(itemId, this, "relation")
    }

    override fun toString() = get().toString()
}
