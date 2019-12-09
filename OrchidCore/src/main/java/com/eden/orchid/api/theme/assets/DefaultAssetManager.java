package com.eden.orchid.api.theme.assets;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.impl.themes.functions.ThumbnailResource;
import javax.inject.Provider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public final class DefaultAssetManager implements AssetManager {

    private final Provider<OrchidContext> context;

    private final Map<String, AssetPage> assets;

    @Inject
    public DefaultAssetManager(Provider<OrchidContext> context) {
        this.context = context;
        this.assets = new HashMap<>();
    }

    @Override
    public AssetPage createAsset(String asset, Object source, String sourceKey) {
        OrchidResource originalResource = context.get().getResourceEntry(asset);
        if (originalResource != null) {
            // don't render the asset immediately. Allow the template to apply transformations to the asset, and it will be
            // rendered lazily when the link for the asset is requested (or not at all if it is never used)
            return new AssetPage(source, sourceKey, new ThumbnailResource(originalResource), "thumbnail", originalResource.getTitle());
        }

        return null;
    }

    @Override
    public AssetPage getActualAsset(AssetPage asset) {
        final String assetKey = asset.getReference().toString();
        if(assets.containsKey(assetKey)) {
            return assets.get(assetKey);
        }
        else {
            return null;
        }
    }

    // Assets should only make it here if it passes the check in a local AssetHolderDelegate, so we don't need to check
    // again. Go ahead and render it now so we can free its resources, and eventually implement an asset pipeline from
    // this point. Inline resources are rendered directly into the page, and should not be rendered as a proper resource
    @Override
    public AssetPage addAsset(AssetPage asset, boolean renderImmediately) {
        final String assetKey = asset.getReference().toString();
        if(assets.containsKey(assetKey)) {
            return assets.get(assetKey);
        }
        else {
            assets.put(assetKey, asset);
            if(renderImmediately) {
                context.get().renderAsset(asset);
            }

            return asset;
        }
    }

    @Override
    public void clearAssets() {
        assets.clear();
    }



}
