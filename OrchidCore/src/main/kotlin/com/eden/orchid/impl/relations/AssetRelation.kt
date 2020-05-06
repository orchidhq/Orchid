package com.eden.orchid.impl.relations

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.Relation
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.assets.AssetManagerDelegate
import com.eden.orchid.api.theme.assets.AssetPage
import com.eden.orchid.api.theme.pages.OrchidPage
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

    private fun createAssetManagerDelegate(context: OrchidContext): AssetManagerDelegate {
        return AssetManagerDelegate(context, this, "relation", null)
    }

    override fun load(): AssetPage? {
        return if (itemId.isNotBlank())
            context.assetManager.createAsset(createAssetManagerDelegate(context), itemId)
        else
            null
    }

    override fun toString() = get().toString()
}
