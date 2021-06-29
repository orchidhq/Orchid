package com.eden.orchid.api.theme.assets

import com.eden.orchid.api.OrchidContext

class AssetManagerDelegate(
    internal val context: OrchidContext,
    /** the source object that is creating this asset */
    val source: Any,
    /** the template key that the source object can refer to when being rendered */
    val sourceKey: String,
    /** an optional prefix to add to the asset's file path */
    val prefix: String?
) {

    private val assets: MutableList<AssetPage> = mutableListOf()
    val allAssets: List<AssetPage> get() = assets

// Add whole pages
// ---------------------------------------------------------------------------------------------------------------------

    @JvmOverloads
    fun addCss(resourceName: String, configure: (CssPageAttributes.() -> Unit)? = null): CssPage {
        val extraCss = if (configure != null) {
            ExtraCss().also {
                it.extractOptions(context, emptyMap())
                it.configure()
            }
        } else null

        return addCss(resourceName, extraCss)
    }

    fun addCss(resourceName: String, configure: CssPageAttributes?): CssPage {
        val resourceSource = context.getDefaultResourceSource(null, context.theme)
        val requestedPage = context.assetManager.createCss(this, resourceSource, resourceName, configure)
        return context.assetManager
            .getActualCss(this, requestedPage, true)
            .also { assets.add(it) }
    }

    @JvmOverloads
    fun addJs(resourceName: String, configure: (JsPageAttributes.() -> Unit)? = null): JsPage {
        val extraJs = if (configure != null) {
            ExtraJs().also {
                it.extractOptions(context, emptyMap())
                it.configure()
            }
        } else null

        return addJs(resourceName, extraJs)
    }

    fun addJs(resourceName: String, configure: JsPageAttributes?): JsPage {
        val resourceSource = context.getDefaultResourceSource(null, context.theme)
        val requestedPage = context.assetManager.createJs(this, resourceSource, resourceName, configure)
        return context.assetManager
            .getActualJs(this, requestedPage, true)
            .also { assets.add(it) }
    }

    override fun toString(): String {
        return "AssetManagerDelegate(source=$source, sourceKey='$sourceKey', prefix=$prefix)"
    }
}
