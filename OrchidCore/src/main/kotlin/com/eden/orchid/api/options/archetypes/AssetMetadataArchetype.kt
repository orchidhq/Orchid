package com.eden.orchid.api.options.archetypes

import clog.Clog
import clog.dsl.format
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionArchetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.theme.assets.AssetPage
import com.eden.orchid.utilities.OrchidUtils
import javax.inject.Inject

@Description(
    value = "Allows this asset to have configurations from data files in the archetype key's directory. " +
        "This is especially useful for binary asset files which cannot have Front Matter. Additional asset configs " +
        "come from a data file at the same path as the asset itself, but in the archetype key's directory.",
    name = "Asset Config"
)
class AssetMetadataArchetype
@Inject
constructor(
    private val context: OrchidContext
) : OptionArchetype {

    override fun getOptions(target: Any, archetypeKey: String): Map<String, Any?>? {
        var data: Map<String, Any?>? = null

        if (target is AssetPage) {
            val metadataFilename = Clog.format(
                "{}/{}/{}",
                OrchidUtils.normalizePath(archetypeKey),
                OrchidUtils.normalizePath(target.resource.reference.originalPath),
                OrchidUtils.normalizePath(target.resource.reference.originalFileName)
            )

            if (metadataFilename.isNotBlank()) {
                data = context.getDataResourceSource(LocalResourceSource).getDatafile(context, metadataFilename)
            }
        }

        return data
    }
}
