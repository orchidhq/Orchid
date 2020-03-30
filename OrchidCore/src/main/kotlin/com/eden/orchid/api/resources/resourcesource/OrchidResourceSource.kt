package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource

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
