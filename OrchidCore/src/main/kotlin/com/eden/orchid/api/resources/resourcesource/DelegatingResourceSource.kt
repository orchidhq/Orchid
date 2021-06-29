package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import java.util.TreeMap

/**
 * An OrchidResourceSource which wraps other resource sources and combines elements from each. For locating a single
 * entry, the first delegate with a matching resource is returned. For locating multiple entries, the resulting list is
 * a combination of the resources collected from _all_ delegates.
 */
class DelegatingResourceSource(
    private val delegates: Collection<OrchidResourceSource>,
    private val scopeFilter: Collection<OrchidResourceSource.Scope>,
    override val priority: Int,
    override val scope: OrchidResourceSource.Scope
) : OrchidResourceSource {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        return delegates
            .asSequence()
            .sortedDescending()
            .filter { scopeFilter.isEmpty() || scopeFilter.contains(it.scope) }
            .map { it.getResourceEntry(context, fileName) }
            .firstOrNull { it != null }
    }

    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource> {
        val entries = TreeMap<String, OrchidResource>()

        delegates
            .asSequence()
            .sorted()
            .filter { scopeFilter.isEmpty() || scopeFilter.contains(it.scope) }
            .map { it.getResourceEntries(context, dirName, fileExtensions, recursive) }
            .filterNot { it.isEmpty() }
            .flatten()
            .forEach { entries[it.reference.originalFullFileName] = it }

        return entries.values.toList()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DelegatingResourceSource

        if (delegates != other.delegates) return false
        if (scopeFilter != other.scopeFilter) return false
        if (priority != other.priority) return false
        if (scope != other.scope) return false

        return true
    }

    private val _hashcode by lazy {
        var result = delegates.hashCode()
        result = 31 * result + scopeFilter.hashCode()
        result = 31 * result + priority
        result = 31 * result + scope.hashCode()
        result
    }
    override fun hashCode(): Int {
        return _hashcode
    }
}
