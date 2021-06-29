package com.eden.orchid.api.resources.resource

import com.eden.orchid.api.theme.assets.AssetPage
import com.eden.orchid.api.resources.thumbnails.Renameable
import com.eden.orchid.api.resources.thumbnails.Resizable
import com.eden.orchid.api.resources.thumbnails.Rotateable
import com.eden.orchid.api.resources.thumbnails.Scalable
import com.eden.orchid.api.theme.permalinks.PermalinkStrategy

class CloudinaryResource(
    resource: ExternalResource
) : ResourceWrapper(resource),
    Rotateable,
    Scalable,
    Resizable,
    Renameable {

    override fun rotate(page: AssetPage, angle: Double) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun scale(page: AssetPage, factor: Double) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun resize(page: AssetPage, width: Int, height: Int, mode: Resizable.Mode) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun rename(
        page: AssetPage,
        permalinkStrategy: PermalinkStrategy,
        permalink: String,
        usePrettyUrl: Boolean
    ) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
