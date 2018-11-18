package com.eden.orchid.api.theme.assets;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.InlineResource;
import com.google.inject.Provider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public final class DefaultAssetManager implements AssetManager {

    private final Provider<OrchidContext> context;

    private final List<AssetPage> assets;

    @Inject
    public DefaultAssetManager(Provider<OrchidContext> context) {
        this.context = context;
        this.assets = new ArrayList<>();
    }

    // Assets should only make it here if it passes the check in a local AssetHolderDelegate, so we don't need to check
    // again. Go ahead and render it now so we can free its resources, and eventually implement an asset pipeline from
    // this point. Inline resources are rendered directly into the page, and should not be rendered as a proper resource
    @Override
    public AssetPage addAsset(AssetPage asset) {
        assets.add(asset);
        if(!(asset.getResource() instanceof InlineResource)) {
            if(context.get().isBinaryExtension(asset.getReference().getOutputExtension())) {
                context.get().renderBinary(asset);
            }
            else {
                context.get().renderRaw(asset);
            }
        }

        return asset;
    }

    @Override
    public void clearAssets() {
        assets.clear();
    }

}
