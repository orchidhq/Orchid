package com.eden.orchid.api.theme.assets;

import java.util.List;

public interface AssetHolder {

    AssetHolder getAssetHolder();

    void addAssets();

    default void withNamespace(String namespace, Runnable cb) {
        getAssetHolder().withNamespace(namespace, cb);
    }

    default JsPage addJs(JsPage jsAsset) {
        return getAssetHolder().addJs(jsAsset);
    }

    default JsPage addJs(String jsAsset) {
        return getAssetHolder().addJs(jsAsset);
    }

    default CssPage addCss(CssPage cssAsset) {
        return getAssetHolder().addCss(cssAsset);
    }

    default CssPage addCss(String cssAsset) {
        return getAssetHolder().addCss(cssAsset);
    }

    default AssetPage addAsset(AssetPage asset) {
        return getAssetHolder().addAsset(asset);
    }

    default AssetPage addAsset(String asset) {
        return getAssetHolder().addAsset(asset);
    }

    default List<JsPage> getScripts() {
        return getAssetHolder().getScripts();
    }

    default List<CssPage> getStyles() {
        return getAssetHolder().getStyles();
    }
    
    default void clearAssets() {
        getAssetHolder().clearAssets();
    }
}
