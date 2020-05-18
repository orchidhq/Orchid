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
    private val delegates: List<OrchidResourceSource>,
    private val scopeFilter: List<OrchidResourceSource.Scope>,
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

}
