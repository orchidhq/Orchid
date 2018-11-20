package com.eden.orchid.impl.themes.functions

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.DoubleDefault
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.ResourceTransformation
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.assets.AssetPage
import com.eden.orchid.api.theme.assets.Resizable
import com.eden.orchid.api.theme.assets.Rotateable
import com.eden.orchid.api.theme.assets.Scalable
import com.eden.orchid.impl.relations.AssetRelation
import com.eden.orchid.utilities.convertOutputStream
import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
import java.io.InputStream
import javax.inject.Inject

@Description(value = "Render an asset and get its URL.", name = "Asset")
class AssetFunction
@Inject
constructor(
        val context: OrchidContext
) : TemplateFunction("asset", false) {

    @Option
    @Description("The path to a resource to render.")
    lateinit var itemId: String

    override fun parameters(): Array<String?> {
        return arrayOf("itemId")
    }

    override fun apply(): Any? {
        val originalResource = context.getLocalResourceEntry(itemId)
        if (originalResource != null) {
            // don't render the asset immediately. Allow the template to apply transformations to the asset, and it will be
            // rendered lazily when the link for the asset is requested (or not at all if it is never used)
            return AssetPage(page, "page", ThumbnailResource(originalResource), "thumbnail", originalResource.title)
        }

        return null
    }
}

// Standard image manipulation functions
//----------------------------------------------------------------------------------------------------------------------

@Description(value = "Lookup and return an asset. The asset will not be rendered until its link is requested.", name = "Asset")
abstract class BaseImageManipulationFunction
@Inject
constructor(
        name: String
) : TemplateFunction(name, false) {

    @Option
    var input: Any? = null

    protected inline fun <reified T> applyInternal(transformation: (AssetPage, T) -> Unit): Any? {
        if (input != null) {
            val asset: AssetPage?

            if (input is AssetPage) {
                asset = input as AssetPage
            }
            else if (input is AssetRelation) {
                asset = (input as AssetRelation).get()
            }
            else {
                asset = null
            }

            if (asset == null) {
                Clog.e("Cannot use '{}' function on null object", name)
            }
            else if (asset.isRendered) {
                Clog.e("Cannot use '{}' function on asset {}, it has already been rendered", name, asset.toString())
            }
            else if (asset.resource !is T) {
                Clog.e("Cannot use '{}' function on asset {}, its resource is not an instance of {}", name, asset.toString(), T::class.java.name)
            }
            else {
                transformation(asset, asset.resource as T)

                return asset
            }
        }

        return null
    }
}

@Description(value = "Rotate an image asset.", name = "Rotate")
class RotateFunction
@Inject
constructor() : BaseImageManipulationFunction("rotate") {

    @Option
    @DoubleDefault(0.0)
    @Description("Set image rotation angle in degrees.")
    var angle: Double = 0.0

    override fun parameters(): Array<String?> {
        return arrayOf("input", "angle")
    }

    override fun apply(): Any? {
        return applyInternal { asset, resource: Rotateable ->
            resource.rotate(asset, angle)
        }
    }
}

@Description(value = "Scale an image asset by a constant factor.", name = "Scale")
class ScaleFunction
@Inject
constructor() : BaseImageManipulationFunction("scale") {

    @Option
    @DoubleDefault(0.0)
    @Description("Set image rotation angle in degrees.")
    var factor: Double = 0.0

    override fun parameters(): Array<String?> {
        return arrayOf("input", "factor")
    }

    override fun apply(): Any? {
        return applyInternal { asset, resource: Scalable ->
            resource.scale(asset, factor)
        }
    }
}

@Description(value = "Resize an image asset to specific dimensions.", name = "Resize")
class ResizeFunction
@Inject
constructor() : BaseImageManipulationFunction("resize") {

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
    @Description("`exact` to stretch image to fit, or `fit` to maintain aspect ratio. Alternatively, you can crop " +
            "the image to the specified dimensions by setting a mode with the cropping position, one of " +
            "[" +
            "`tl`, `tc`, `tr`, " +
            "`cl`, `c`, `cr`, " +
            "`bl`, `bc`, `br`" +
            "]"
    )
    lateinit var mode: Resizable.Mode

    override fun parameters(): Array<String?> {
        return arrayOf("input", "width", "height", "mode")
    }

    override fun apply(): Any? {
        return applyInternal { asset, resource: Resizable ->
            resource.resize(asset, width, height, mode)
        }
    }
}

class ThumbnailResource(
        resource: OrchidResource
) : ResourceTransformation(resource),
        OptionsHolder,
        Rotateable,
        Scalable,
        Resizable {

    init {
        rawContent = ""
        content = ""
        embeddedData = null

        contentStreamTransformations = mutableListOf()
    }

    override fun rotate(page: AssetPage, angle: Double) {
        page.reference.fileName = page.reference.originalFileName + "_rotate-${angle}"
        contentStreamTransformations.add { input -> rotateTransformation(input, angle, reference.outputExtension) }
    }

    override fun scale(page: AssetPage, factor: Double) {
        page.reference.fileName = page.reference.originalFileName + "_scale-${factor}"
        contentStreamTransformations.add { input -> scaleTransformation(input, factor, reference.outputExtension) }
    }

    override fun resize(page: AssetPage, width: Int, height: Int, mode: Resizable.Mode) {
        page.reference.fileName = page.reference.originalFileName + "_${width}x${height}_${mode.name}"
        contentStreamTransformations.add { input -> resizeTransformation(input, width, height, mode, reference.outputExtension) }
    }

    companion object {
        fun rotateTransformation(input: InputStream, angle: Double, outputFormat: String): InputStream {
            return convertOutputStream { safeOs ->
                Thumbnails
                        .of(input)
                        .rotate(angle)
                        .scale(1.0)
                        .outputFormat(outputFormat)
                        .toOutputStream(safeOs)
            }
        }

        fun scaleTransformation(input: InputStream, factor: Double, outputFormat: String): InputStream {
            return convertOutputStream { safeOs ->
                Thumbnails
                        .of(input)
                        .scale(factor)
                        .outputFormat(outputFormat)
                        .toOutputStream(safeOs)
            }
        }

        fun resizeTransformation(input: InputStream, width: Int, height: Int, mode: Resizable.Mode, outputFormat: String): InputStream {
            return convertOutputStream { safeOs ->
                val thumbnailBuilder = Thumbnails.of(input)

                when (mode) {
                    Resizable.Mode.exact -> thumbnailBuilder.forceSize(width, height)
                    Resizable.Mode.fit   -> thumbnailBuilder.size(width, height)
                    Resizable.Mode.bl    -> thumbnailBuilder.size(width, height).crop(Positions.BOTTOM_LEFT)
                    Resizable.Mode.bc    -> thumbnailBuilder.size(width, height).crop(Positions.BOTTOM_CENTER)
                    Resizable.Mode.br    -> thumbnailBuilder.size(width, height).crop(Positions.BOTTOM_RIGHT)
                    Resizable.Mode.cl    -> thumbnailBuilder.size(width, height).crop(Positions.CENTER_LEFT)
                    Resizable.Mode.c     -> thumbnailBuilder.size(width, height).crop(Positions.CENTER)
                    Resizable.Mode.cr    -> thumbnailBuilder.size(width, height).crop(Positions.CENTER_RIGHT)
                    Resizable.Mode.tl    -> thumbnailBuilder.size(width, height).crop(Positions.TOP_LEFT)
                    Resizable.Mode.tc    -> thumbnailBuilder.size(width, height).crop(Positions.TOP_CENTER)
                    Resizable.Mode.tr    -> thumbnailBuilder.size(width, height).crop(Positions.TOP_RIGHT)
                }

                thumbnailBuilder.outputFormat(outputFormat)
                thumbnailBuilder.toOutputStream(safeOs)
            }
        }
    }

}
