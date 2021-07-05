package com.eden.orchid.impl.resources.resourcesource

import com.eden.orchid.api.resources.resourcesource.CommonExternalAliasesResourceSource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource

class LocalCommonExternalAliasesResourceSource : OrchidResourceSource by CommonExternalAliasesResourceSource(
    Integer.MAX_VALUE,
    LocalResourceSource
)
