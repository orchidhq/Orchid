package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.AbstractTheme
import com.eden.orchid.utilities.LRUCache
import com.eden.orchid.utilities.computeIfAbsent

internal class CachingResourceSource(
    private val delegate: OrchidResourceSource,
    private val cache: LRUCache<CachingResourceSourceCacheKey, OrchidResource?>,
    private val theme: AbstractTheme?,
    private val delegateScopes: List<OrchidResourceSource.Scope>
) : OrchidResourceSource by delegate {

    private val themeKey = theme?.key ?: ""
    private val themeHashcode = theme.hashCode()

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        val key = CachingResourceSourceCacheKey(fileName, themeKey, themeHashcode, delegateScopes)
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
}

data class CachingResourceSourceCacheKey(
    val fileName: String,
    val themeKey: String,
    val themeHashcode: Int,
    val delegateScopes: List<OrchidResourceSource.Scope>
)

fun OrchidResourceSource.cached(
    cache: LRUCache<CachingResourceSourceCacheKey, OrchidResource?>,
    theme: AbstractTheme?,
    delegateScopes: List<OrchidResourceSource.Scope>
): OrchidResourceSource {
    return CachingResourceSource(
        this,
        cache,
        theme,
        delegateScopes
    )
}
