package com.eden.orchid.impl.generators

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.assets.AssetPage
import com.eden.orchid.api.theme.pages.OrchidPage
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Description(
    value = "Add additional arbitrary assets to your site. Assets added from themes, pages, and components " + "are automatically rendered to your site, this is just for additional static assets.",
    name = "Assets"
)
class AssetsGenerator @Inject
constructor(context: OrchidContext) : OrchidGenerator(context,
    GENERATOR_KEY, OrchidGenerator.PRIORITY_INIT) {

    @Option
    @Description("Set which local resource directories you want to copy static assets from.")
    lateinit var sourceDirs: List<AssetDirectory>

    override fun startIndexing(): List<OrchidPage>? {
        if (EdenUtils.isEmpty(sourceDirs)) {
            val dir = AssetDirectory()
            dir.sourceDir = "assets"
            dir.assetFileExtensions = emptyArray()
            dir.isRecursive = true
            sourceDirs = listOf(dir)
        }

        sourceDirs.stream()
            .flatMap<OrchidResource> { dir ->
                context.getLocalResourceEntries(
                    dir.sourceDir,
                    if (!EdenUtils.isEmpty(dir.assetFileExtensions)) dir.assetFileExtensions else null,
                    dir.isRecursive
                ).stream()
            }
            .map { resource ->
                AssetPage(
                    null, null,
                    resource,
                    resource.reference.fileName,
                    resource.reference.fileName
                )
            }
            .peek { asset -> asset.reference.isUsePrettyUrl = false }
            .forEach { asset -> context.globalAssetHolder.addAsset(asset) }

        return null
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {

    }

    override fun getCollections(): List<OrchidCollection<*>>? {
        return null
    }

    // Helpers
    //----------------------------------------------------------------------------------------------------------------------

    class AssetDirectory : OptionsHolder {

        @Option
        @StringDefault("assets")
        @Description("Set which local resource directories you want to copy static assets from.")
        lateinit var sourceDir: String

        @Option
        @Description("Restrict the file extensions used for the assets in this directory.")
        lateinit var assetFileExtensions: Array<String>

        @Option
        @BooleanDefault(true)
        @Description("Whether to include subdirectories of this directory")
        var isRecursive: Boolean = true
    }

    companion object {

        val GENERATOR_KEY = "assets"
    }

}

