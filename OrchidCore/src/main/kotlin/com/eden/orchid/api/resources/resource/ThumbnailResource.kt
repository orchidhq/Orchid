package com.eden.orchid.api.resources.resource

import com.eden.orchid.api.theme.assets.AssetPage
import com.eden.orchid.api.resources.thumbnails.Renameable
import com.eden.orchid.api.resources.thumbnails.Resizable
import com.eden.orchid.api.resources.thumbnails.Rotateable
import com.eden.orchid.api.resources.thumbnails.Scalable
import com.eden.orchid.api.theme.permalinks.PermalinkStrategy
import com.eden.orchid.utilities.convertOutputStream
import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
import org.apache.commons.io.FilenameUtils
import javax.imageio.ImageIO

class ThumbnailResource(
    resource: OrchidResource
) : ResourceTransformation(resource),
    Rotateable,
    Scalable,
    Resizable,
    Renameable {

    override fun rotate(page: AssetPage, angle: Double) {
        page.reference.fileName = page.reference.originalFileName + "_rotate-$angle"
        contentStreamTransformations.add { input ->
            convertOutputStream(page.context) { safeOs ->
                Thumbnails
                    .of(input)
                    .rotate(angle)
                    .scale(1.0)
                    .outputFormat(reference.outputExtension)
                    .toOutputStream(safeOs)
            }
        }
    }

    override fun scale(page: AssetPage, factor: Double) {
        page.reference.fileName = page.reference.originalFileName + "_scale-$factor"
        contentStreamTransformations.add { input ->
            convertOutputStream(page.context) { safeOs ->
                Thumbnails
                    .of(input)
                    .scale(factor)
                    .outputFormat(reference.outputExtension)
                    .toOutputStream(safeOs)
            }
        }
    }

    override fun resize(page: AssetPage, width: Int, height: Int, mode: Resizable.Mode) {
        page.reference.fileName = page.reference.originalFileName + "_${width}x${height}_${mode.name}"
        contentStreamTransformations.add { input ->
            convertOutputStream(page.context) { safeOs ->
                val thumbnailBuilder = Thumbnails.of(input)

                when (mode) {
                    Resizable.Mode.exact -> thumbnailBuilder.forceSize(width, height)
                    Resizable.Mode.fit -> thumbnailBuilder.size(width, height)
                    Resizable.Mode.bl -> thumbnailBuilder.size(width, height).crop(Positions.BOTTOM_LEFT)
                    Resizable.Mode.bc -> thumbnailBuilder.size(width, height).crop(Positions.BOTTOM_CENTER)
                    Resizable.Mode.br -> thumbnailBuilder.size(width, height).crop(Positions.BOTTOM_RIGHT)
                    Resizable.Mode.cl -> thumbnailBuilder.size(width, height).crop(Positions.CENTER_LEFT)
                    Resizable.Mode.c -> thumbnailBuilder.size(width, height).crop(Positions.CENTER)
                    Resizable.Mode.cr -> thumbnailBuilder.size(width, height).crop(Positions.CENTER_RIGHT)
                    Resizable.Mode.tl -> thumbnailBuilder.size(width, height).crop(Positions.TOP_LEFT)
                    Resizable.Mode.tc -> thumbnailBuilder.size(width, height).crop(Positions.TOP_CENTER)
                    Resizable.Mode.tr -> thumbnailBuilder.size(width, height).crop(Positions.TOP_RIGHT)
                }

                thumbnailBuilder.outputFormat(reference.outputExtension)
                thumbnailBuilder.toOutputStream(safeOs)
            }
        }
    }

    override fun rename(
        page: AssetPage,
        permalinkStrategy: PermalinkStrategy,
        permalink: String,
        usePrettyUrl: Boolean
    ) {
        val originalExtension = page.reference.outputExtension
        val newExtension = FilenameUtils.getExtension(permalink)
        permalinkStrategy.applyPermalink(page, permalink, usePrettyUrl)

        if (newExtension.isNotBlank() && newExtension != originalExtension) {
            contentStreamTransformations.add { input ->
                if (ImageIO.getWriterFormatNames().any { it == newExtension }) {
                    convertOutputStream(page.context) { safeOs ->
                        Thumbnails
                            .of(input)
                            .scale(1.0)
                            .outputFormat(newExtension)
                            .toOutputStream(safeOs)
                    }
                } else {
                    input
                }
            }
        }
    }
}
