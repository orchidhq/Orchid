package com.eden.orchid.api.theme.assets

import com.eden.orchid.api.theme.permalinks.PermalinkStrategy

interface Renameable {
    fun rename(page: AssetPage, permalinkStrategy: PermalinkStrategy, permalink: String, usePrettyUrl: Boolean)
}
