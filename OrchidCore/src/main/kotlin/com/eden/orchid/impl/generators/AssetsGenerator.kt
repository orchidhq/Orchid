package com.eden.orchid.impl.generators

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.generators.emptyModel
import com.eden.orchid.api.generators.modelOf
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.ImpliedKey
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.options.annotations.Validate
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.theme.assets.AssetManagerDelegate
import com.eden.orchid.api.theme.assets.AssetPage
import java.util.Arrays
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.constraints.NotBlank

@Singleton
@Description(
    value = "Add additional arbitrary assets to your site. Assets added from themes, pages, and components " + "are automatically rendered to your site, this is just for additional static assets.",
    name = "Assets"
)
class AssetsGenerator : OrchidGenerator<OrchidGenerator.Model>(GENERATOR_KEY, Stage.WARM_UP) {

    @Option
    @Description("Set which local resource directories you want to copy static assets from.")
    @ImpliedKey(typeKey = "sourceDir")
    @StringDefault("assets/media")
    lateinit var sourceDirs: List<AssetDirectory>

    private fun createAssetManagerDelegate(context: OrchidContext): AssetManagerDelegate {
        return AssetManagerDelegate(context, this, "generator", null)
    }

    override fun startIndexing(context: OrchidContext): Model {
        val delegate = createAssetManagerDelegate(context)
        val assetPages = sourceDirs
            .flatMap { dir ->
                context.getDefaultResourceSource(LocalResourceSource, null).getResourceEntries(
                    context,
                    dir.sourceDir,
                    if (!EdenUtils.isEmpty(dir.assetFileExtensions)) dir.assetFileExtensions else null,
                    dir.recursive
                )
            }
            .map { resource ->
                context
                    .assetManager
                    .createAsset(delegate, resource.reference.originalFullFileName, LocalResourceSource)
                    .let { context.assetManager.getActualAsset(delegate, it, false) }
            }

        return modelOf { assetPages }
    }

    override fun startGeneration(context: OrchidContext, model: Model) {
        model.allPages.filterIsInstance<AssetPage>().forEach { context.renderAsset(it) }
    }

    // Helpers
//----------------------------------------------------------------------------------------------------------------------

    @Validate
    class AssetDirectory : OptionsHolder {

        @Option
        @NotBlank
        @Description("Set which local resource directories you want to copy static assets from.")
        lateinit var sourceDir: String

        @Option
        @Description("Restrict the file extensions used for the assets in this directory.")
        lateinit var assetFileExtensions: Array<String>

        @Option
        @BooleanDefault(true)
        @Description("Whether to include subdirectories of this directory")
        var recursive: Boolean = true

        override fun toString(): String {
            return "AssetDirectory(sourceDir='$sourceDir', assetFileExtensions=${Arrays.toString(assetFileExtensions)}, isRecursive=$recursive)"
        }

    }

    companion object {
        val GENERATOR_KEY = "assets"
    }

}

