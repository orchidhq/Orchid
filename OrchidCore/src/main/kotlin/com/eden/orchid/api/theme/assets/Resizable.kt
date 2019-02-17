package com.eden.orchid.api.theme.assets

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
