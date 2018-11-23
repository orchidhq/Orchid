package com.eden.orchid.api.theme.assets;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultAssetManager.class)
public interface AssetManager {

    AssetPage createAsset(String asset, Object source, String sourceKey);
    AssetPage getActualAsset(AssetPage asset);
    AssetPage addAsset(AssetPage asset, boolean renderImmediately);
    void clearAssets();

}
