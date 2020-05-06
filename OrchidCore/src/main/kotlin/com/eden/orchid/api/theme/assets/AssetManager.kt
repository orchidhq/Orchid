package com.eden.orchid.api.theme.assets

import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource
import com.google.inject.ImplementedBy
import java.util.stream.Stream

@ImplementedBy(DefaultAssetManager::class)
interface AssetManager {

    /**
     * Stream through all assets that have been cached.
     *
     * @return a stream of all cached assets
     */
    val allAssetPages: Stream<AssetPage>

    /**
     * Clear the asset cache
     */
    fun clearAssets()

    /**
     * Create a new generic AssetPage. After creation, it should be configured by the caller, then checked against the
     * cached assets with [.getActualAsset]. Do not return the asset from this method directly, it should only be
     * used for creating and configuring potentially-new assets.
     *
     * @param asset the asset to lookup and convert into an AssetPage
     * @param origin the origin AssetManagerDelegate that is creating this asset
     *
     * @return a new page. Do not render directly, first get the actual page (potentially cached) from [.getActualAsset].
     */
    fun createAsset(origin: AssetManagerDelegate, asset: String, scopes: OrchidResourceSource.Scope? = null): AssetPage

    /**
     * Create a new CssPage. After creation, it should be configured by the caller, then checked against the
     * cached assets with [.getActualCss]. Do not return the asset from this method directly, it should only be
     * used for creating and configuring potentially-new assets.
     *
     * @param asset the asset to lookup and convert into an CssPage
     * @param origin the origin AssetManagerDelegate that is creating this asset
     *
     * @return a new page. Do not render directly, first get the actual page (potentially cached) from [.getActualCss].
     */
    fun createCss(origin: AssetManagerDelegate, asset: String, scopes: OrchidResourceSource.Scope? = null, configure: CssPageAttributes? = null): CssPage

    /**
     * Create a new JsPage. After creation, it should be configured by the caller, then checked against the
     * cached assets with [.createJs]. Do not return the asset from this method directly, it should only be
     * used for creating and configuring potentially-new assets.
     *
     * @param asset the asset to lookup and convert into an JsPage
     * @param origin the origin AssetManagerDelegate that is creating this asset
     *
     * @return a new page. Do not render directly, first get the actual page (potentially cached) from [.createJs].
     */
    fun createJs(origin: AssetManagerDelegate, asset: String, scopes: OrchidResourceSource.Scope? = null, configure: JsPageAttributes? = null): JsPage

    /**
     * Checks the given asset against the cache. If any asset matches the provided asset, return that instance instead.
     * Assets are compared for equality by their [.hashCode()].
     *
     * If the asset passed to this method does not exist in the cache, then that instance will be added to the cache,
     * and if `renderImmediately` is true, also rendered to file before returning from this method.
     *
     * The asset returned from this method should be considered final, and no more changes may be made to it. However,
     * you may still use this asset to copy and create new assets from. If no changes are made to those copies, then
     * they, themselves, will resolve to the original asset. If changes are made to the copies, then they are a
     * different asset and will not interfere with the original.
     *
     * @param asset the asset to check against the cache
     * @param renderImmediately if the asset is not cached, whether it should be rendered before returning to this method
     * @return the actual asset to refer to, which may or may not be the same instance that was passed in.
     */
    fun getActualAsset(origin: AssetManagerDelegate, asset: AssetPage, renderImmediately: Boolean): AssetPage

    /**
     * This method is the same as [.getActualAsset], but filters the cache against CssPages only
     *
     * @param asset the CSS asset to check against the cache
     * @param renderImmediately if the CSS asset is not cached, whether it should be rendered before returning to this method
     * @return the actual CSS asset to refer to, which may or may not be the same instance that was passed in.
     */
    fun getActualCss(origin: AssetManagerDelegate, asset: CssPage, renderImmediately: Boolean): CssPage

    /**
     * This method is the same as [.getActualAsset], but filters the cache against JsPages only
     *
     * @param asset the JS asset to check against the cache
     * @param renderImmediately if the JS asset is not cached, whether it should be rendered before returning to this method
     * @return the actual JS asset to refer to, which may or may not be the same instance that was passed in.
     */
    fun getActualJs(origin: AssetManagerDelegate, asset: JsPage, renderImmediately: Boolean): JsPage
}
