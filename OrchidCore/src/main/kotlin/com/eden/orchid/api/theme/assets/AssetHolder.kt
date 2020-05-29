package com.eden.orchid.api.theme.assets

interface AssetHolder {

    fun getAssetHolder(): AssetHolder

    fun addAssets()

    fun withNamespace(namespace: String?, cb: ()->Unit) {
        getAssetHolder().withNamespace(namespace, cb)
    }

    fun addJs(jsAsset: JsPage, configure: JsPage.()->Unit = {}): JsPage {
        return getAssetHolder().addJs(jsAsset).also(configure)
    }

    fun addJs(jsAsset: String, configure: JsPage.()->Unit = {}): JsPage {
        return getAssetHolder().addJs(jsAsset).also(configure)
    }

    fun addCss(cssAsset: CssPage, configure: CssPage.()->Unit = {}): CssPage {
        return getAssetHolder().addCss(cssAsset).also(configure)
    }

    fun addCss(cssAsset: String, configure: CssPage.()->Unit = {}): CssPage {
        return getAssetHolder().addCss(cssAsset).also(configure)
    }

    fun getScripts(): List<JsPage> {
        return getAssetHolder().getScripts()
    }

    fun getStyles(): List<CssPage> {
        return getAssetHolder().getStyles()
    }

    fun clearAssets() {
        getAssetHolder().clearAssets()
    }
}
