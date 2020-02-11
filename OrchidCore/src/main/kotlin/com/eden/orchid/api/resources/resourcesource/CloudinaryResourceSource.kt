package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.assets.AssetPage
import com.eden.orchid.api.resources.thumbnails.Renameable
import com.eden.orchid.api.resources.thumbnails.Resizable
import com.eden.orchid.api.resources.thumbnails.Rotateable
import com.eden.orchid.api.resources.thumbnails.Scalable
import com.eden.orchid.api.theme.permalinks.PermalinkStrategy
import java.nio.file.Path

class CloudinaryResourceSource(
    override val priority: Int,
    override val scope: OrchidResourceSource.Scope
) : OrchidResourceSource {

    override fun getResourceEntry(
        context: OrchidContext,
        fileName: String
    ): OrchidResource? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
