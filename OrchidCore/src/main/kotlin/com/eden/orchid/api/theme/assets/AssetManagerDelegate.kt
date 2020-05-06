package com.eden.orchid.api.theme.assets

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import java.util.ArrayList
import java.util.stream.Collectors
import javax.inject.Inject

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
//----------------------------------------------------------------------------------------------------------------------

    @JvmOverloads
    fun addCss(resourceName: String, configure: (CssPageAttributes.() -> CssPageAttributes)? = null): CssPage {
        val requestedPage = context.assetManager
            .createCss(this, resourceName, configure = configure?.let { it(ExtraCss().also { it.extractOptions(context, emptyMap()) }) })
        return context.assetManager
            .getActualCss(this, requestedPage, true)
            .also { assets.add(it) }
    }

    @JvmOverloads
    fun addJs(resourceName: String, configure: (JsPageAttributes.() -> JsPageAttributes)? = null): JsPage {
        val requestedPage = context.assetManager
            .createJs(this, resourceName, configure = configure?.let { it(ExtraJs().also { it.extractOptions(context, emptyMap()) }) })
        return context.assetManager
            .getActualJs(this, requestedPage, true)
            .also { assets.add(it) }
    }

    override fun toString(): String {
        return "AssetManagerDelegate(source=$source, sourceKey='$sourceKey', prefix=$prefix)"
    }
}


