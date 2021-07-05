package com.eden.orchid.api.resources.thumbnails

import com.eden.orchid.api.theme.assets.AssetPage

interface Rotateable {
    fun rotate(page: AssetPage, angle: Double)
}
