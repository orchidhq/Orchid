package com.eden.orchid.impl.resources

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resourcesource.FileResourceSource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.google.inject.Provider
import com.google.inject.name.Named

import javax.inject.Inject

class LocalFileResourceSource
@Inject
constructor(
        context: Provider<OrchidContext>,
        @Named("src") resourcesDir: String
) : FileResourceSource(context, resourcesDir, Integer.MAX_VALUE), LocalResourceSource
