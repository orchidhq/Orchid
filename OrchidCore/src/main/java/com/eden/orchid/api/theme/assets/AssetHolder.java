package com.eden.orchid.api.theme.assets;

import com.eden.orchid.api.theme.pages.OrchidPage;

import java.util.List;

public interface AssetHolder {

    void addJs(OrchidPage jsAsset);
    void addCss(OrchidPage cssAsset);

    List<OrchidPage> getScripts();
    List<OrchidPage> getStyles();

    void flushJs();
    void flushCss();
    
    default void clearAssets() {
        this.flushJs();
        this.flushCss();
    }

}
