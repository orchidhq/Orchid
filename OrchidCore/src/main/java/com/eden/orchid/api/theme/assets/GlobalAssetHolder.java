package com.eden.orchid.api.theme.assets;

import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class GlobalAssetHolder implements AssetHolder {

    private final List<OrchidPage> js;
    private final List<OrchidPage> css;

    @Inject
    public GlobalAssetHolder() {
        this.js = new ArrayList<>();
        this.css = new ArrayList<>();
    }

    @Override
    public void addAssets() {
        throw new UnsupportedOperationException("GlobalAssetHolder cannot add its own assets");
    }

    @Override
    public AssetHolder getAssetHolder() {
        return this;
    }

    // Assets should only make it here if it passes the check in a local AssetHolderDelegate, so we don't need to check again
    @Override
    public void addJs(OrchidPage jsAsset) {
        js.add(jsAsset);
    }

    // Assets should only make it here if it passes the check in a local AssetHolderDelegate, so we don't need to check again
    @Override
    public void addCss(OrchidPage cssAsset) {
        css.add(cssAsset);
    }

    @Override
    public List<OrchidPage> getScripts() {
        return js;
    }

    @Override
    public List<OrchidPage> getStyles() {
        return css;
    }

    @Override
    public void flushJs() {
        js.clear();
    }

    @Override
    public void flushCss() {
        css.clear();
    }

    @Override
    public void clearAssets() {
        flushJs();
        flushCss();
    }
}
