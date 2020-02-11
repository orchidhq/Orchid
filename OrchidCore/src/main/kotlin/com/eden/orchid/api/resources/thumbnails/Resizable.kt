package com.eden.orchid.api.resources.thumbnails

import com.eden.orchid.api.theme.assets.AssetPage

interface Resizable {
    fun resize(page: AssetPage, width: Int, height: Int, mode: Mode)

    enum class Mode {
        exact,
        fit,
        bl,
        bc,
        br,
        cl,
        c,
        cr,
        tl,
        tc,
        tr,
    }
}
