package com.eden.orchid.api.theme.assets;

import java.util.List;

public interface AssetHolder {

    AssetHolder getAssetHolder();

    void addAssets();

    default void withNamespace(String namespace, Runnable cb) {
        getAssetHolder().withNamespace(namespace, cb);
    }

    default void addJs(AssetPage jsAsset) {
        getAssetHolder().addJs(jsAsset);
    }

    default void addJs(String jsAsset) {
        getAssetHolder().addJs(jsAsset);
    }

    default void addCss(AssetPage cssAsset) {
        getAssetHolder().addCss(cssAsset);
    }

    default void addCss(String cssAsset) {
        getAssetHolder().addCss(cssAsset);
    }

    default List<AssetPage> getScripts() {
        return getAssetHolder().getScripts();
    }

    default List<AssetPage> getStyles() {
        return getAssetHolder().getStyles();
    }

    default void flushJs() {
        getAssetHolder().flushJs();
    }

    default void flushCss() {
        getAssetHolder().flushCss();
    }
    
    default void clearAssets() {
        getAssetHolder().clearAssets();
    }

    default boolean shouldDownloadExternalAssets() {
        return getAssetHolder().shouldDownloadExternalAssets();
    }
}
