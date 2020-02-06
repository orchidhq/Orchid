package com.eden.orchid.impl.resources

import com.eden.orchid.api.resources.resourcesource.ExternalResourceSource
import com.eden.orchid.api.resources.resourcesource.FileResourceSource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.google.inject.name.Named
import javax.inject.Inject

class LocalExternalResourceSource : ExternalResourceSource(Integer.MAX_VALUE, LocalResourceSource)
