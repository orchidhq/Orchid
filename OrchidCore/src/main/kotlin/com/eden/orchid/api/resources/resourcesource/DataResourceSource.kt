package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext

interface DataResourceSource: OrchidResourceSource {
    fun getDatafile(context: OrchidContext, fileName: String): Map<String, Any?>?
    fun loadData(context: OrchidContext, name: String): Map<String, Any?>
}
