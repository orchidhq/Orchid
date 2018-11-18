package com.eden.orchid.impl.themes.functions

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.DoubleDefault
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.assets.AssetPage
import com.eden.orchid.api.theme.assets.Resizable
import com.eden.orchid.api.theme.assets.Rotateable
import com.eden.orchid.api.theme.assets.Scalable
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.impl.relations.AssetRelation
import com.eden.orchid.utilities.convertOutputStream
import net.coobird.thumbnailator.Thumbnails
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
        if(originalResource != null) {
            val newReference = OrchidReference(originalResource.reference)
            val newResource = ThumbnailResource(originalResource, newReference)

            // don't render the asset immediately. Allow the template to apply transformations to the asset, and it will be
            // rendered lazily when the link for the asset is requested (or not at all if it is never used)
            return AssetPage(page, "page", newResource, "thumbnail", "")
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

    protected inline fun <reified T> applyInternal(transformation: (AssetPage, T)->Unit): Any? {
        if(input != null) {
            val asset: AssetPage?

            if(input is AssetPage) {
                asset = input as AssetPage
            }
            else if(input is AssetRelation) {
                asset = (input as AssetRelation).get()
            }
            else {
                asset = null
            }

            if(asset == null) {
                Clog.e("Cannot use '{}' function on null object", name)
            }
            else if(asset.isRendered) {
                Clog.e("Cannot use '{}' function on asset {}, it has already been rendered", name, asset.toString())
            }
            else if(asset.resource !is T) {
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

    @Option @DoubleDefault(0.0)
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

    @Option @DoubleDefault(0.0)
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

    @Option @IntDefault(-1)
    @Description("Set image rotation angle in degrees.")
    var width: Int = -1

    @Option @IntDefault(-1)
    @Description("Set image rotation angle in degrees.")
    var height: Int = -1

    override fun parameters(): Array<String?> {
        return arrayOf("input", "width", "height")
    }

    override fun apply(): Any? {
        return applyInternal { asset, resource: Resizable ->
            resource.resize(asset, width, height)
        }
    }
}






class ThumbnailResource(
        val resource: OrchidResource,
        newReference: OrchidReference
) : OrchidResource(newReference), OptionsHolder, Rotateable, Scalable, Resizable {

    var resizeWidth: Int = -1
    var resizeHeight: Int = -1
    var scaleFactor: Double = 0.0
    var rotateAngle: Double = 0.0

    init {
        rawContent = ""
        content = ""
        embeddedData = null

        newReference.fileName = newReference.originalFileName
        newReference.isUsePrettyUrl = false
    }

    override fun rotate(page: AssetPage, angle: Double) {
        rotateAngle = angle
        page.reference.fileName = page.reference.originalFileName + "_rotate-${rotateAngle}"

    }

    override fun scale(page: AssetPage, factor: Double) {
        scaleFactor = factor
        page.reference.fileName = page.reference.originalFileName + "_scale-${scaleFactor}"
    }

    override fun resize(page: AssetPage, width: Int, height: Int) {
        resizeWidth = width
        resizeHeight = height
        page.reference.fileName = page.reference.originalFileName + "_${resizeWidth}x${resizeHeight}"
    }

    override fun getContentStream(): InputStream? {
        return convertOutputStream { safeOs ->
            val thumbnailBuilder = Thumbnails.of(resource.contentStream)

            if(resizeWidth > 0 && resizeHeight > 0) {
                thumbnailBuilder.size(resizeWidth, resizeHeight)
            }
            else if(scaleFactor > 0) {
                thumbnailBuilder.scale(scaleFactor)
            }
            else {
                thumbnailBuilder.scale(1.0)
            }

            if(rotateAngle != 0.0) {
                thumbnailBuilder.rotate(rotateAngle)
            }

            thumbnailBuilder.outputFormat(resource.reference.extension)
            thumbnailBuilder.toOutputStream(safeOs)
        }
    }

}
