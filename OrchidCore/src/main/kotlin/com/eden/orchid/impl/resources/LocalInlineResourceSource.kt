package com.eden.orchid.impl.resources

import com.eden.orchid.api.resources.resourcesource.InlineResourceSource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource

class LocalInlineResourceSource : InlineResourceSource(Integer.MAX_VALUE - 2, LocalResourceSource)
