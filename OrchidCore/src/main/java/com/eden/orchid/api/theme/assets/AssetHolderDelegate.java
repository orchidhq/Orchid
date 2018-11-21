package com.eden.orchid.api.theme.assets;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.ExternalResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import org.apache.commons.io.FilenameUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public final class AssetHolderDelegate implements AssetHolder {

    protected final OrchidContext context;

    private final List<AssetPage> assets;

    private final Object source;
    private final String sourceKey;

    private String prefix;

    @Inject
    public AssetHolderDelegate(OrchidContext context, Object source, String sourceKey) {
        this.context = context;
        this.source = source;
        this.sourceKey = sourceKey;
        this.assets = new ArrayList<>();
    }

// AssetHolder implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void addAssets() {
        throw new UnsupportedOperationException("AssetHolderDelegate cannot add its own assets");
    }

    @Override
    public AssetHolder getAssetHolder() {
        return this;
    }

    @Override
    public List<JsPage> getScripts() {
        return assets
                .stream()
                .filter(asset -> asset instanceof JsPage)
                .map(asset -> (JsPage) asset)
                .collect(Collectors.toList());
    }

    @Override
    public List<CssPage> getStyles() {
        return assets
                .stream()
                .filter(asset -> asset instanceof CssPage)
                .map(asset -> (CssPage) asset)
                .collect(Collectors.toList());
    }

    @Override
    public void clearAssets() {
        assets.clear();
    }

    @Override
    public boolean shouldDownloadExternalAssets() {
        return context.isProduction();
    }

    @Override
    public synchronized void withNamespace(String namespace, Runnable cb) {
        prefix = OrchidUtils.normalizePath(namespace);
        cb.run();
        prefix = null;
    }

// Add whole pages
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public JsPage addJs(JsPage jsAsset) {
        return addJs(jsAsset, true);
    }

    private JsPage addJs(JsPage jsAsset, boolean renderImmediately) {
        return addAssetInternal(jsAsset, "js", renderImmediately);
    }

    @Override
    public CssPage addCss(CssPage cssAsset) {
        return addCss(cssAsset, true);
    }

    private CssPage addCss(CssPage cssAsset, boolean renderImmediately) {
        return addAssetInternal(cssAsset, "css", renderImmediately);
    }

    @Override
    public AssetPage addAsset(AssetPage asset) {
        return addAsset(asset, true);
    }

    private AssetPage addAsset(AssetPage asset, boolean renderImmediately) {
        return addAssetInternal(asset, null, renderImmediately);
    }

// Add assets by string
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public JsPage addJs(String jsAsset) {
        return addAssetInternal(jsAsset, "JS", true, JsPage::new, this::addJs);
    }

    @Override
    public CssPage addCss(String cssAsset) {
        return addAssetInternal(cssAsset, "CSS", true, CssPage::new, this::addCss);
    }

    @Override
    public AssetPage addAsset(String asset) {
        return addAssetInternal(asset, "", true, AssetPage::new, this::addAsset);
    }

// internals
//----------------------------------------------------------------------------------------------------------------------

    private boolean validAsset(OrchidPage asset, String targetExtension) {
        return asset.getReference().getOutputExtension().equalsIgnoreCase(targetExtension);
    }

    private <T extends AssetPage> T addAssetInternal(T asset, String expectedOutputExtension, boolean renderImmediately) {
        if(expectedOutputExtension == null || validAsset(asset, expectedOutputExtension)) {
            asset.getReference().setUsePrettyUrl(false);
            AssetPage actualAsset = context.getAssetManager().addAsset(asset, renderImmediately);
            assets.add(actualAsset);
            return asset;
        }
        else {
            Clog.w("#{$1} is not a valid #{$2} asset, perhaps you are missing a #{$3}->#{$4} Compiler extension?",
                    asset.getReference().getOriginalFullFileName(),
                    expectedOutputExtension.toUpperCase(),
                    asset.getReference().getOutputExtension(),
                    expectedOutputExtension);
            return null;
        }
    }

    private <T extends AssetPage> T addAssetInternal(String asset, String assetTypeName, boolean renderImmediately, CreateAssetInterface<T> creator, BiConsumer<T, Boolean> adder) {
        OrchidResource resource = context.getResourceEntry(asset);
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
            T page = creator.createAsset(source, sourceKey, resource, FilenameUtils.getBaseName(asset), null);
            if(setPrefix) {
                page.getReference().setPath(prefix + "/" + page.getReference().getOriginalPath());
            }
            adder.accept(page, renderImmediately);
            return page;
        }
        else {
            Clog.w("Could not find {} asset: {}", assetTypeName, asset);
        }

        return null;
    }

    private interface CreateAssetInterface<T extends AssetPage> {
        T createAsset(Object source, String sourceKey, OrchidResource resource, String key, String title);
    }

}
