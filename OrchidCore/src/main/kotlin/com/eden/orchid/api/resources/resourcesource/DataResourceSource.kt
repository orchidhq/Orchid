package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext

interface DataResourceSource: OrchidResourceSource {
    fun getData(context: OrchidContext, dataName: String): Map<String, Any>
}
