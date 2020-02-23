package com.eden.orchid.impl.resources.resourcesource

import com.eden.orchid.api.resources.resourcesource.InlineResourceSource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource

class LocalInlineResourceSource : OrchidResourceSource by InlineResourceSource(Integer.MAX_VALUE - 2, LocalResourceSource)
