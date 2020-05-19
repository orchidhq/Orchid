package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource

interface TemplateResourceSource: OrchidResourceSource {
    fun getResourceEntry(
        context: OrchidContext,
        templateSubdir: String,
        possibleFileNames: List<String>
    ): OrchidResource?
}
