package com.eden.orchid.impl.themes.functions

import clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.DoubleDefault
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.thumbnails.Renameable
import com.eden.orchid.api.resources.thumbnails.Resizable
import com.eden.orchid.api.resources.thumbnails.Rotateable
import com.eden.orchid.api.resources.thumbnails.Scalable
import com.eden.orchid.api.theme.assets.AssetManagerDelegate
import com.eden.orchid.api.theme.assets.AssetPage
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.permalinks.PermalinkStrategy
import com.eden.orchid.impl.relations.AssetRelation
import javax.inject.Inject

@Description(value = "Render an asset and get its URL.", name = "Asset")
class AssetFunction : TemplateFunction("asset", false) {

    @Option
    @Description("The path to a resource to render.")
    lateinit var itemId: String

    override fun parameters() = arrayOf(::itemId.name)

    private fun createAssetManagerDelegate(context: OrchidContext, page: OrchidPage?): AssetManagerDelegate {
        return if (page != null) {
            AssetManagerDelegate(context, page, "page", null)
        } else {
            AssetManagerDelegate(context, this, name, null)
        }
    }

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        return runCatching {
            context.assetManager.createAsset(
                createAssetManagerDelegate(context, page),
                context.getDefaultResourceSource(null, page?.theme),
                itemId
            )
        }.getOrNull()
    }
}

// Standard image manipulation functions
// ---------------------------------------------------------------------------------------------------------------------

@Description(
    value = "Lookup and return an asset. The asset will not be rendered until its link is requested.",
    name = "Asset"
)
abstract class BaseImageManipulationFunction(name: String, isSafeString: Boolean = false) : TemplateFunction(name, isSafeString) {

    @Option
    var input: Any? = null

    private fun createAssetManagerDelegate(context: OrchidContext, page: OrchidPage?): AssetManagerDelegate {
        return if (page != null) {
            AssetManagerDelegate(context, page, "page", null)
        } else {
            AssetManagerDelegate(context, this, name, null)
        }
    }

    protected fun getAssetPage(context: OrchidContext, page: OrchidPage?): AssetPage? {
        if (input != null) {
            val asset: AssetPage?

            if (input is AssetPage) {
                asset = input as AssetPage
            } else if (input is AssetRelation) {
                // always create a new asset from the relation, since the original is probably already rendered and we
                // want to manipulate the asset and render it differently
                asset = (input as AssetRelation).load()
            } else if (input is String) {
                asset = context.assetManager.createAsset(
                    createAssetManagerDelegate(context, page),
                    context.getDefaultResourceSource(null, page?.theme),
                    input!!.toString()
                )
            } else {
                asset = null
            }
            return asset
        }
        return null
    }

    protected inline fun <reified T> applyInternal(
        context: OrchidContext,
        page: OrchidPage?,
        transformation: (AssetPage, T) -> Unit
    ): Any? {
        if (input != null) {
            val asset: AssetPage? = getAssetPage(context, page)

            if (asset == null) {
                Clog.e("Cannot use '{}' function on null object", name)
            } else if (asset.isRendered) {
                Clog.e("Cannot use '{}' function on asset {}, it has already been rendered", name, asset.toString())
            } else if (asset.resource !is T) {
                Clog.e(
                    "Cannot use '{}' function on asset {}, its resource is not an instance of {}",
                    name,
                    asset.toString(),
                    T::class.java.name
                )
            } else {
                transformation(asset, asset.resource as T)

                return asset
            }
        }

        return null
    }
}

@Description(value = "Rotate an image asset.", name = "Rotate")
class RotateFunction : BaseImageManipulationFunction("rotate") {

    @Option
    @DoubleDefault(0.0)
    @Description("Set image rotation angle in degrees.")
    var angle: Double = 0.0

    override fun parameters() = arrayOf(::input.name, ::angle.name)

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        return applyInternal(context, page) { asset, resource: Rotateable ->
            resource.rotate(asset, angle)
        }
    }
}

@Description(value = "Scale an image asset by a constant factor.", name = "Scale")
class ScaleFunction : BaseImageManipulationFunction("scale") {

    @Option
    @DoubleDefault(0.0)
    @Description("Set image rotation angle in degrees.")
    var factor: Double = 0.0

    override fun parameters() = arrayOf(::input.name, ::factor.name)

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        return applyInternal(context, page) { asset, resource: Scalable ->
            resource.scale(asset, factor)
        }
    }
}

@Description(value = "Resize an image asset to specific dimensions.", name = "Resize")
class ResizeFunction : BaseImageManipulationFunction("resize") {

    @Option
    @IntDefault(-1)
    @Description("Set image rotation angle in degrees.")
    var width: Int = -1

    @Option
    @IntDefault(-1)
    @Description("Set image rotation angle in degrees.")
    var height: Int = -1

    @Option
    @StringDefault("fit")
    @Description(
        "`exact` to stretch image to fit, or `fit` to maintain aspect ratio. Alternatively, you can crop " +
            "the image to the specified dimensions by setting a mode with the cropping position, one of " +
            "[" +
            "`tl`, `tc`, `tr`, " +
            "`cl`, `c`, `cr`, " +
            "`bl`, `bc`, `br`" +
            "]"
    )
    lateinit var mode: Resizable.Mode

    override fun parameters() = arrayOf(::input.name, ::width.name, ::height.name, ::mode.name)

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        return applyInternal(context, page) { asset, resource: Resizable ->
            resource.resize(asset, width, height, mode)
        }
    }
}

@Description(value = "Rename an image asset.", name = "Rename")
class RenameFunction
@Inject
constructor(private val permalinkStrategy: PermalinkStrategy) : BaseImageManipulationFunction("rename") {

    @Option
    @Description("Set image rotation angle in degrees.")
    lateinit var permalink: String

    @Option
    @BooleanDefault(false)
    @Description("Set image rotation angle in degrees.")
    var usePrettyUrl: Boolean = false

    override fun parameters() = arrayOf(::input.name, ::permalink.name, ::usePrettyUrl.name)

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        return applyInternal(context, page) { asset, resource: Renameable ->
            resource.rename(asset, permalinkStrategy, permalink, usePrettyUrl)
        }
    }
}

@Description(value = "Render an asset as an img HTML tag.", name = "Image")
class ImageFunction : BaseImageManipulationFunction("img", true) {

    @Option
    lateinit var alt: String

    override fun parameters() = arrayOf(::input.name, ::alt.name, ::alt.name)

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        val asset: AssetPage? = getAssetPage(context, page)

        return """ <img src="${asset?.link}" alt="$alt"> """
    }
}
