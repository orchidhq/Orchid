package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.AbstractTheme
import com.eden.orchid.utilities.LRUCache
import com.eden.orchid.utilities.computeIfAbsent

class CachingResourceSource
constructor(
    private val delegate: OrchidResourceSource,
    private val cache: LRUCache<CacheKey, OrchidResource?>,
    private val theme: AbstractTheme?,
    private val delegateScopes: List<OrchidResourceSource.Scope>
) : OrchidResourceSource by delegate {

    private val themeKey = theme?.key ?: ""
    private val themeHashcode = theme.hashCode()

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        val key = CacheKey(fileName, themeKey, themeHashcode, delegateScopes)
        return cache.computeIfAbsent(key) {
            delegate.getResourceEntry(context, fileName)
        }
    }

    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource> {
        return delegate.getResourceEntries(context, dirName, fileExtensions, recursive)
    }

    data class CacheKey(
        val fileName: String,
        val themeKey: String,
        val themeHashcode: Int,
        val delegateScopes: List<OrchidResourceSource.Scope>
    )
}

fun OrchidResourceSource.cached(
    cache: LRUCache<CachingResourceSource.CacheKey, OrchidResource?>,
    theme: AbstractTheme?,
    delegateScopes: List<OrchidResourceSource.Scope>
): CachingResourceSource {
    return CachingResourceSource(
        this,
        cache,
        theme,
        delegateScopes
    )
}
