package com.eden.orchid.impl.generators

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.generators.emptyModel
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.ImpliedKey
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.options.annotations.Validate
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
class AssetsGenerator : OrchidGenerator<OrchidGenerator.Model>(GENERATOR_KEY, PRIORITY_INIT) {

    @Option
    @Description("Set which local resource directories you want to copy static assets from.")
    @ImpliedKey("sourceDir")
    @StringDefault("assets/media")
    lateinit var sourceDirs: List<AssetDirectory>

    override fun startIndexing(context: OrchidContext): Model {
        sourceDirs
            .flatMap { dir ->
                context.getLocalResourceEntries(
                    dir.sourceDir,
                    if (!EdenUtils.isEmpty(dir.assetFileExtensions)) dir.assetFileExtensions else null,
                    dir.recursive
                )
            }
            .map { resource ->
                AssetPage(
                    null, null,
                    resource,
                    resource.reference.fileName,
                    resource.reference.fileName
                ).apply {
                    reference.isUsePrettyUrl = false
                }
            }
            .forEach { asset ->
                context.assetManager.addAsset(asset, true)
            }

        return emptyModel()
    }

    override fun getCollections(
        context: OrchidContext,
        model: Model
    ): List<OrchidCollection<*>> {
        return emptyList()
    }

    override fun startGeneration(context: OrchidContext, model: Model) {

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

