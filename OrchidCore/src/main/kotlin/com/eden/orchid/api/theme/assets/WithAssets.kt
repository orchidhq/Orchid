package com.eden.orchid.api.theme.assets

import com.eden.orchid.api.OrchidContext

interface WithAssets {

    fun createAssetManagerDelegate(context: OrchidContext): AssetManagerDelegate
    fun getExtraCss(): List<ExtraCss>
    fun getExtraJs(): List<ExtraJs>

    /**
     * Override this method to add CSS or JS assets to this page
     */
    fun loadAssets(delegate: AssetManagerDelegate)
}
