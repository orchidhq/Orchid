package com.eden.orchid.api.theme.assets

import com.eden.orchid.api.theme.components.ComponentHolder
import com.eden.orchid.api.theme.pages.OrchidPage
import java.util.ArrayList

class CombinedAssetHolder(
    private val delegates: List<AssetManagerDelegate>
) {

    private val allAssets: List<AssetPage> by lazy {
        delegates.flatMap { it.allAssets }
    }

    val scripts: List<JsPage> by lazy {
        allAssets.filterIsInstance<JsPage>().dedupeAssets()
    }

    val styles: List<CssPage> by lazy {
        allAssets.filterIsInstance<CssPage>().dedupeAssets()
    }

    private fun <T : AssetPage> List<T>.dedupeAssets(): List<T> {
        // TODO: dedupe asset hashCodes (not resource paths) for performance
        return this.distinctBy { it.reference.toString(it.context) }
    }
}

fun OrchidPage.initializePageAssets(): CombinedAssetHolder {
    val delegatePairs: MutableList<Pair<WithAssets, AssetManagerDelegate>> = ArrayList()
    // create delegate from theme
    (theme to theme.createAssetManagerDelegate(context))
        .also { delegatePairs.add(it) }

    // create delegates from components
     (this.componentHolders + theme.componentHolders)
        .flatMap { it.get(this) }
        .map { it to it.createAssetManagerDelegate(context) }
        .also { delegatePairs.addAll(it) }

    // create delegate from this page
    (this to this.createAssetManagerDelegate(context))
        .also { delegatePairs.add(it) }

    // load all assets from each individual delegate
    val delegates = delegatePairs
        .map { (source, assetManagerDelegate) ->
            source.loadAssets(assetManagerDelegate)
            source.getExtraCss().forEach { assetManagerDelegate.addCss(it.asset) { it } }
            source.getExtraJs().forEach { assetManagerDelegate.addJs(it.asset) { it } }
            assetManagerDelegate
        }

    // combine into a single location, and use that to get the list of scripts/styles
    return CombinedAssetHolder(delegates)
}
