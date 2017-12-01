package com.eden.orchid.api.theme.assets;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class GlobalAssetHolder implements AssetHolder {

    private final List<AssetPage> js;
    private final List<AssetPage> css;

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
    public void addJs(AssetPage jsAsset) {
        js.add(jsAsset);
    }

    // Assets should only make it here if it passes the check in a local AssetHolderDelegate, so we don't need to check again
    @Override
    public void addCss(AssetPage cssAsset) {
        css.add(cssAsset);
    }

    @Override
    public List<AssetPage> getScripts() {
        return js;
    }

    @Override
    public List<AssetPage> getStyles() {
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
