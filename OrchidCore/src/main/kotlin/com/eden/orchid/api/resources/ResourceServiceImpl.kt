package com.eden.orchid.api.resources

import com.eden.orchid.Orchid.Lifecycle.ClearCache
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.events.On
import com.eden.orchid.api.events.OrchidEventListener
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resourcesource.CachingResourceSourceCacheKey
import com.eden.orchid.api.resources.resourcesource.DataResourceSource
import com.eden.orchid.api.resources.resourcesource.DelegatingResourceSource
import com.eden.orchid.api.resources.resourcesource.FlexibleResourceSource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource
import com.eden.orchid.api.resources.resourcesource.TemplateResourceSource
import com.eden.orchid.api.resources.resourcesource.cached
import com.eden.orchid.api.resources.resourcesource.flexible
import com.eden.orchid.api.resources.resourcesource.useForData
import com.eden.orchid.api.resources.resourcesource.useForTemplates
import com.eden.orchid.api.theme.AbstractTheme
import com.eden.orchid.utilities.LRUCache
import com.eden.orchid.utilities.SuppressedWarnings
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Description(value = "How Orchid locates resources.", name = "Resources")
@Archetype(value = ConfigArchetype::class, key = "services.resources")
@JvmSuppressWildcards
class ResourceServiceImpl
@Inject
constructor(
    private val resourceSources: Set<OrchidResourceSource>
) : ResourceService, OrchidEventListener {

    private val resourceCache: LRUCache<CachingResourceSourceCacheKey, OrchidResource?> = LRUCache()

    // TODO: this is not available early enough because we need options to be loaded before they can be extracted, but
    //  this class needs to be extracted before we can properly load the options to ignore specific files. It's a
    //  circular dependency that needs to be refactored. Maybe create a "primordial resource source" for the initial
    //  bootstrapping of Orchid?
    @Option
    @StringDefault(".DS_store", ".localized")
    @Description("A list of filenames to globally filter out files from being sourced. Should be used primarily for ignoring pesky hidden or system files that are not intended to be used as site content.")
    private var ignoredFilenames: Array<String> = arrayOf(".DS_store", ".localized")

    override fun initialize(context: OrchidContext) {}

    override fun getDefaultResourceSource(
        scopes: OrchidResourceSource.Scope?,
        theme: AbstractTheme?
    ): OrchidResourceSource {
        val delegates = if (theme != null) resourceSources + theme.resourceSource else resourceSources
        val validScopes = scopes?.let { listOf(it) } ?: emptyList()

        return DelegatingResourceSource(
            delegates,
            validScopes,
            0,
            LocalResourceSource
        ).cached(resourceCache, theme, validScopes)
    }

    override fun getFlexibleResourceSource(
        scopes: OrchidResourceSource.Scope?,
        theme: AbstractTheme?
    ): FlexibleResourceSource {
        return getDefaultResourceSource(scopes, theme).flexible()
    }

    override fun getTemplateResourceSource(
        scopes: OrchidResourceSource.Scope?,
        theme: AbstractTheme
    ): TemplateResourceSource {
        return getDefaultResourceSource(scopes, theme).useForTemplates(theme)
    }

    override fun getDataResourceSource(scopes: OrchidResourceSource.Scope?): DataResourceSource {
        return getDefaultResourceSource(scopes, null).useForData()
    }

// Delombok
//----------------------------------------------------------------------------------------------------------------------

    override fun getIgnoredFilenames(): Array<String> {
        return ignoredFilenames
    }

    fun setIgnoredFilenames(ignoredFilenames: Array<String>) {
        this.ignoredFilenames = ignoredFilenames
    }

// Cache Implementation
//----------------------------------------------------------------------------------------------------------------------

    @On(ClearCache::class)
    @Suppress(SuppressedWarnings.UNUSED_PARAMETER)
    fun onClearCache(event: ClearCache?) {
        resourceCache.clear()
    }
}
