package com.eden.orchid.api.resources.thumbnails

import com.eden.orchid.api.theme.assets.AssetPage
import com.eden.orchid.api.theme.permalinks.PermalinkStrategy

interface Renameable {
    fun rename(page: AssetPage, permalinkStrategy: PermalinkStrategy, permalink: String, usePrettyUrl: Boolean)
}
