package com.eden.orchid.impl.resources

import com.eden.orchid.api.resources.resourcesource.ExternalResourceSource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource

class LocalExternalResourceSource : ExternalResourceSource(Integer.MAX_VALUE, LocalResourceSource)
