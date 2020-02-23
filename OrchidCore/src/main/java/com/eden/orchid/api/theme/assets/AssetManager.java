package com.eden.orchid.api.theme.assets;

import com.google.inject.ImplementedBy;

import java.util.stream.Stream;

@ImplementedBy(DefaultAssetManager.class)
public interface AssetManager {

    AssetPage createAsset(String asset, Object source, String sourceKey);
    AssetPage getActualAsset(AssetPage asset);
    AssetPage addAsset(AssetPage asset, boolean renderImmediately);
    Stream<AssetPage> getAllAssetPages();
    void clearAssets();

}
