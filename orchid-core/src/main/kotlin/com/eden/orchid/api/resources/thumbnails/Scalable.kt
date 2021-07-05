package com.eden.orchid.api.resources.thumbnails

import com.eden.orchid.api.theme.assets.AssetPage

interface Scalable {
    fun scale(page: AssetPage, factor: Double)
}
