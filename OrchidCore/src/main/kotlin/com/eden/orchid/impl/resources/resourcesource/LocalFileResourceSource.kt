package com.eden.orchid.impl.resources.resourcesource

import com.eden.orchid.api.resources.resourcesource.FileResourceSource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource
import com.google.inject.name.Named
import java.io.File
import javax.inject.Inject

class LocalFileResourceSource
@Inject
constructor(
    @Named("src") resourcesDir: String
) : OrchidResourceSource by FileResourceSource(
    File(resourcesDir).toPath(),
    Integer.MAX_VALUE - 2,
    LocalResourceSource
)
