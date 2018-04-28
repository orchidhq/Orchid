package com.eden.orchid.api.theme.assets;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.ExternalResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.AbstractTheme;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import org.apache.commons.io.FilenameUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class AssetHolderDelegate implements AssetHolder {

    protected final OrchidContext context;

    public static final String JS_EXT = "js";
    public static final String CSS_EXT = "css";

    private final List<AssetPage> assets;

    private final Object source;
    private final String sourceKey;

    private String prefix;

    private final AbstractTheme theme;

    @Inject
    public AssetHolderDelegate(OrchidContext context, Object source, String sourceKey) {
        this.context = context;
        this.source = source;
        this.sourceKey = sourceKey;
        this.theme = (source instanceof AbstractTheme) ? (AbstractTheme) source : null;
        this.assets = new ArrayList<>();
    }

    @Override
    public void addAssets() {
        throw new UnsupportedOperationException("AssetHolderDelegate cannot add its own assets");
    }

    @Override
    public AssetHolder getAssetHolder() {
        return this;
    }

    @Override
    public AssetPage addJs(AssetPage jsAsset) {
        if(validAsset(jsAsset, JS_EXT)) {
            jsAsset.getReference().setUsePrettyUrl(false);
            assets.add(jsAsset);
            context.getGlobalAssetHolder().addAsset(jsAsset);
            return jsAsset;
        }
        else {
            Clog.w("#{$1} is not a valid JS asset, perhaps you are missing a #{$2}->#{$3} Compiler extension?",
                    jsAsset.getReference().getOriginalFullFileName(),
                    jsAsset.getReference().getOutputExtension(),
                    JS_EXT);
        }

        return null;
    }

    @Override
    public AssetPage addJs(String jsAsset) {
        OrchidResource resource = context.getResourceEntry(jsAsset);
        if(resource != null) {
            boolean setPrefix = !EdenUtils.isEmpty(prefix);
            if(resource instanceof ExternalResource) {
                if(shouldDownloadExternalAssets()) {
                    ((ExternalResource) resource).setDownload(true);
                }
                else {
                    setPrefix = false;
                }
            }
            AssetPage page = new AssetPage(source, sourceKey, resource, FilenameUtils.getBaseName(jsAsset));
            if(setPrefix) {
                page.getReference().setPath(prefix + "/" + page.getReference().getOriginalPath());
            }
            addJs(page);
            return page;
        }
        else {
            Clog.w("Could not find JS asset: {}", jsAsset);
        }

        return null;
    }

    @Override
    public AssetPage addCss(AssetPage cssAsset) {
        if(validAsset(cssAsset, CSS_EXT)) {
            cssAsset.getReference().setUsePrettyUrl(false);
            assets.add(cssAsset);
            context.getGlobalAssetHolder().addAsset(cssAsset);
            return cssAsset;
        }
        else {
            Clog.w("#{$1} is not a valid CSS asset, perhaps you are missing a #{$2}->#{$3} Compiler extension?",
                    cssAsset.getReference().getOriginalFullFileName(),
                    cssAsset.getReference().getOutputExtension(),
                    CSS_EXT);
        }
        return null;
    }

    @Override
    public AssetPage addCss(String cssAsset) {
        OrchidResource resource = context.getResourceEntry(cssAsset);
        if(resource != null) {
            boolean setPrefix = !EdenUtils.isEmpty(prefix);
            if(resource instanceof ExternalResource) {
                if(shouldDownloadExternalAssets()) {
                    ((ExternalResource) resource).setDownload(true);
                }
                else {
                    setPrefix = false;
                }
            }
            AssetPage page = new AssetPage(source, sourceKey, resource, FilenameUtils.getBaseName(cssAsset));
            if(setPrefix) {
                page.getReference().setPath(prefix + "/" + page.getReference().getOriginalPath());
            }
            addCss(page);
            return page;
        }
        else {
            Clog.w("Could not find CSS asset: {}", cssAsset);
        }

        return null;
    }

    @Override
    public AssetPage addAsset(AssetPage asset) {
        asset.getReference().setUsePrettyUrl(false);
        assets.add(asset);
        context.getGlobalAssetHolder().addAsset(asset);
        return asset;
    }

    @Override
    public AssetPage addAsset(String asset) {
        OrchidResource resource = context.getResourceEntry(theme, asset);
        if(resource != null) {
            boolean setPrefix = !EdenUtils.isEmpty(prefix);
            if(resource instanceof ExternalResource) {
                if(shouldDownloadExternalAssets()) {
                    ((ExternalResource) resource).setDownload(true);
                }
                else {
                    setPrefix = false;
                }
            }
            AssetPage page = new AssetPage(source, sourceKey, resource, FilenameUtils.getBaseName(asset));
            if(setPrefix) {
                page.getReference().setPath(prefix + "/" + page.getReference().getOriginalPath());
            }
            addAsset(page);
            return page;
        }
        else {
            Clog.w("Could not find asset: {}", asset);
        }

        return null;
    }

    @Override
    public List<AssetPage> getScripts() {
        return assets
                .stream()
                .filter(asset -> asset.getReference().getOutputExtension().equals(JS_EXT))
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetPage> getStyles() {
        return assets
                .stream()
                .filter(asset -> asset.getReference().getOutputExtension().equals(CSS_EXT))
                .collect(Collectors.toList());
    }

    @Override
    public void clearAssets() {
        assets.clear();
    }

    private boolean validAsset(OrchidPage asset, String targetExtension) {
        return asset.getReference().getOutputExtension().equalsIgnoreCase(targetExtension);
    }

    public boolean shouldDownloadExternalAssets() {
        return context.isProduction();
    }

    public void withNamespace(String namespace, Runnable cb) {
        prefix = OrchidUtils.normalizePath(namespace);
        cb.run();
        prefix = null;
    }
}
