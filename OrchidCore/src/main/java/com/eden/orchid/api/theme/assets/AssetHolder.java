package com.eden.orchid.api.theme.assets;

import com.eden.orchid.api.theme.pages.OrchidPage;

import java.util.List;

public interface AssetHolder {

    AssetHolder getAssetHolder();

    default void addAssets() {
        getAssetHolder().addAssets();
    }

    default void addJs(OrchidPage jsAsset) {
        getAssetHolder().addJs(jsAsset);
    }

    default void addJs(String jsAsset) {
        getAssetHolder().addJs(jsAsset);
    }

    default void addCss(OrchidPage cssAsset) {
        getAssetHolder().addCss(cssAsset);
    }

    default void addCss(String cssAsset) {
        getAssetHolder().addCss(cssAsset);
    }

    default List<OrchidPage> getScripts() {
        return getAssetHolder().getScripts();
    }

    default List<OrchidPage> getStyles() {
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
}
