package com.eden.orchid.api.resources.resourcesource

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.AbstractTheme
import com.eden.orchid.utilities.LRUCache
import com.eden.orchid.utilities.computeIfAbsent
import java.util.concurrent.atomic.AtomicInteger

class CachingResourceSource
constructor(
    private val cache: LRUCache<CacheKey, OrchidResource?>,
    private val theme: AbstractTheme?,
    private val delegate: OrchidResourceSource,
    private val delegateScopes: List<OrchidResourceSource.Scope>
) : OrchidResourceSource by delegate {

    private val themeKey = theme?.key ?: ""
    private val themeHashcode = theme.hashCode()

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        val key = CacheKey(fileName, themeKey, themeHashcode, delegateScopes)
        val requests = cacheRequests.incrementAndGet()
        return cache.computeIfAbsent(key) {
            val misses = cacheMisses.incrementAndGet()
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

    companion object {
        val cacheRequests = AtomicInteger(0)
        val cacheMisses = AtomicInteger(0)
    }
}
