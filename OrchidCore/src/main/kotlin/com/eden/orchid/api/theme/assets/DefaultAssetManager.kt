package com.eden.orchid.api.theme.assets

import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.resources.resource.ThumbnailResource
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource
import com.eden.orchid.api.theme.pages.OrchidReference
import java.util.stream.Stream
import javax.inject.Singleton

@Singleton
class DefaultAssetManager : AssetManager {

    // TODO: make this a cache, not a map
    // TODO: have the keys be asset hashCodes (not resource paths) for improved performance
    private val assets: MutableMap<String, AssetPage> = mutableMapOf()

    override val allAssetPages: Stream<AssetPage> get() = assets.values.stream()

    override fun clearAssets() {
        assets.clear()
    }

    override fun createAsset(origin: AssetManagerDelegate, asset: String, scopes: OrchidResourceSource.Scope?): AssetPage {
        val requestedResource: OrchidResource? = origin.context.getResourceEntry(asset, scopes)

        checkNotNull(requestedResource) { "requested asset '$asset' could not be found" }

        return AssetPage(
            origin,
            ThumbnailResource(requestedResource),
            "thumbnail",
            requestedResource.reference.title
        ).also { it.configureReferences() }
    }

    override fun createCss(origin: AssetManagerDelegate, asset: String, scopes: OrchidResourceSource.Scope?, configure: CssPageAttributes?): CssPage {
        val requestedResource: OrchidResource? = origin.context.getResourceEntry(asset, scopes)

        checkNotNull(requestedResource) { "requested asset '$asset' could not be found" }

        return CssPage(
            origin,
            requestedResource,
            "css",
            requestedResource.reference.title
        ).also {
            if(configure != null) {
                it.applyAttributes(configure)
            }
            it.configureReferences()
        }
    }

    override fun createJs(origin: AssetManagerDelegate, asset: String, scopes: OrchidResourceSource.Scope?, configure: JsPageAttributes?): JsPage {
        val requestedResource: OrchidResource? = origin.context.getResourceEntry(asset, scopes)

        checkNotNull(requestedResource) { "requested asset '$asset' could not be found" }

        return JsPage(
            origin,
            requestedResource,
            "js",
            requestedResource.reference.title
        ).also {
            if(configure != null) {
                it.applyAttributes(configure)
            }
            it.configureReferences()
        }
    }

    override fun getActualAsset(origin: AssetManagerDelegate, asset: AssetPage, renderImmediately: Boolean): AssetPage {
//        val assetKey = asset.hashCode()
        val assetKey = asset.reference.toString(asset.context)

        if (!assets.containsKey(assetKey)) {
            if (renderImmediately) {
                origin.context.renderAsset(asset)
            }
            assets[assetKey] = asset
        }

        return assets[assetKey]!!
    }

    override fun getActualCss(origin: AssetManagerDelegate, asset: CssPage, renderImmediately: Boolean): CssPage {
        val actualAsset = getActualAsset(origin, asset, renderImmediately)

        check(actualAsset is CssPage) { "Cached asset with hashcode ${actualAsset.hashCode()} is not a CssPage" }

        return actualAsset
    }

    override fun getActualJs(origin: AssetManagerDelegate, asset: JsPage, renderImmediately: Boolean): JsPage {
        val actualAsset = getActualAsset(origin, asset, renderImmediately)

        check(actualAsset is JsPage) { "Cached asset with hashcode ${actualAsset.hashCode()} is not a JsPage" }

        return actualAsset
    }
}
