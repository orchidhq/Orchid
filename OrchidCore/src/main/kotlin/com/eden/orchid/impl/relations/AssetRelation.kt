package com.eden.orchid.impl.relations

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.Relation
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.assets.AssetPage
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.impl.themes.functions.ThumbnailResource
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
        val originalResource = context.getLocalResourceEntry(itemId)
        if(originalResource != null) {
            val newReference = OrchidReference(originalResource.reference)
            val newResource = ThumbnailResource(originalResource, newReference)

            // don't render the asset immediately. Allow the template to apply transformations to the asset, and it will be
            // rendered lazily when the link for the asset is requested (or not at all if it is never used)
            return AssetPage(this, "relation", newResource, "thumbnail", "")
        }

        return null
    }

    override fun toString() = get().toString()
}
