package com.eden.orchid.api.resources.resourcesource

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.utilities.OrchidUtils

class LoggingResourceSource(
    private val delegate: OrchidResourceSource
): OrchidResourceSource by delegate {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        Clog.d("getResourceEntry(fileName=$fileName)")
        return delegate.getResourceEntry(context, fileName)
    }

    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource> {
        Clog.d("getResourceEntry(dirName=$dirName, fileExtensions=$fileExtensions, recursive=$recursive)")
        return delegate.getResourceEntries(context, dirName, fileExtensions, recursive)
    }
}

fun OrchidResourceSource.withLogging(): LoggingResourceSource {
    return LoggingResourceSource(this)
}
