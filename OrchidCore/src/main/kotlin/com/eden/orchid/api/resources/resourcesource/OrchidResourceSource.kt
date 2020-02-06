package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource

/**
 * OrchidResourceSource define the resource lookup order. Resources are looked up in the following order:
 *
 * 1) Local sources
 * 2) The currently active theme
 * 3) Plugin sources
 *
 * This makes it so that any resource defined in a plugin or theme can always be overridden by your local resoure
 * sources. Likewise, any resource defined in a plugin can be overridden by the theme.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
interface OrchidResourceSource : Comparable<OrchidResourceSource> {

    val priority: Int
    val scope: Scope

    fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource?

    fun getResourceEntries(context: OrchidContext, dirName: String, fileExtensions: Array<String>?, recursive: Boolean): List<OrchidResource>

    override fun compareTo(other: OrchidResourceSource): Int {
        val scopePriorityDifference = scope.scopePriority.compareTo(other.scope.scopePriority)
        return if(scopePriorityDifference != 0) scopePriorityDifference
        else priority.compareTo(other.priority)
    }

    interface Scope {
        val scopePriority: Int
    }

}
