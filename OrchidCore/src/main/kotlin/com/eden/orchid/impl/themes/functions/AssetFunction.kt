package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.AllOptions
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.DoubleDefault
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.assets.AssetPage
import com.eden.orchid.api.theme.pages.OrchidReference
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
    lateinit var path: String

    @Option @IntDefault(-1)
    @Description("Set the target width of the image. Should also set `height`")
    var width: Int = -1

    @Option @IntDefault(-1)
    @Description("Set the target height of the image. Should also set `width`")
    var height: Int = -1

    @Option @DoubleDefault(0.0)
    @Description("Set image rotation in degrees.")
    var rotate: Double = 0.0

    @Option @DoubleDefault(0.0)
    @Description("Scale the image by a factor rather. ")
    var scale: Double = 0.0

    @AllOptions
    lateinit var allData: Map<String, Any>

    override fun parameters(): Array<String?> {
        return arrayOf("path", "width", "height", "rotate", "scale")
    }

    override fun apply(): Any? {
        val originalResource = context.getLocalResourceEntry(path)
        if(originalResource != null) {
            val newReference = OrchidReference(originalResource.reference)
            val newResource = ThumbnailResource(originalResource, newReference, allData)
            val newPage = AssetPage(page, "page", newResource, "thumbnail", "")

            // don't render the asset immediately. Allow the template to apply transformations to the asset, and it will be
            // rendered lazily when the link for the asset is requested (or not at all if it is never used)
            return context.assetManager.addAsset(newPage, false)
        }

        return null
    }
}

class ThumbnailResource(
        val resource: OrchidResource,
        newReference: OrchidReference,
        options: Map<String, Any>
) : OrchidResource(newReference), OptionsHolder {

    @Option @IntDefault(-1)
    var width: Int = -1

    @Option @IntDefault(-1)
    var height: Int = -1

    @Option @DoubleDefault(0.0)
    var rotate: Double = 0.0

    @Option @DoubleDefault(0.0)
    var scale: Double = 0.0

    init {
        rawContent = ""
        content = ""
        embeddedData = null

        extractOptions(context, options)

        var newFilename = newReference.originalFileName

        if(width > 0 && height > 0) {
            newFilename += "_${width}x${height}"
        }
        else if(scale > 0) {
            newFilename += "_scale-${scale}"
        }
        if(rotate != 0.0) {
            newFilename += "_rotate-${rotate}"
        }

        newReference.fileName = newFilename
        newReference.isUsePrettyUrl = false
    }

    override fun getContentStream(): InputStream? {
        return convertOutputStream { safeOs ->
            val thumbnailBuilder = Thumbnails.of(resource.contentStream)

            if(width > 0 && height > 0) {
                thumbnailBuilder.size(width, height)
            }
            else if(scale > 0) {
                thumbnailBuilder.scale(scale)
            }
            else {
                thumbnailBuilder.scale(1.0)
            }

            if(rotate != 0.0) {
                thumbnailBuilder.rotate(rotate)
            }

            thumbnailBuilder.outputFormat(resource.reference.extension)
            thumbnailBuilder.toOutputStream(safeOs)
        }
    }

}
