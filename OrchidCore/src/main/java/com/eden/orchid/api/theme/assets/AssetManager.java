package com.eden.orchid.api.theme.assets;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultAssetManager.class)
public interface AssetManager {

    AssetPage addAsset(AssetPage asset);
    void clearAssets();

}
