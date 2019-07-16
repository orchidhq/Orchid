package com.eden.orchid.impl.resources

import com.eden.orchid.api.resources.resourcesource.FileResourceSource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.google.inject.name.Named
import javax.inject.Inject

class LocalFileResourceSource
@Inject
constructor(
    @Named("src") resourcesDir: String
) : FileResourceSource(resourcesDir, Integer.MAX_VALUE), LocalResourceSource
