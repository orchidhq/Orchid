package com.eden.orchid.impl.resources.resourcesource

import com.eden.orchid.api.resources.resourcesource.ExternalResourceSource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource

class LocalExternalResourceSource : OrchidResourceSource by ExternalResourceSource(
    Integer.MAX_VALUE - 1,
    LocalResourceSource
)
