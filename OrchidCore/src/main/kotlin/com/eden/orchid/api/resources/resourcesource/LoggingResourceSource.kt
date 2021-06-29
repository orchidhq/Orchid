package com.eden.orchid.api.resources.resourcesource

import clog.Clog
import clog.dsl.tag
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource

class LoggingResourceSource(
    private val delegate: OrchidResourceSource,
    val tag: String
) : OrchidResourceSource by delegate {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        Clog.tag(tag).d("getResourceEntry(fileName=$fileName)")
        return delegate.getResourceEntry(context, fileName)
    }

    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource> {
        Clog.tag(tag).d("getResourceEntry(dirName=$dirName, fileExtensions=$fileExtensions, recursive=$recursive)")
        return delegate.getResourceEntries(context, dirName, fileExtensions, recursive)
    }
}

fun OrchidResourceSource.withLogging(tag: String = "LoggingResourceSource"): LoggingResourceSource {
    return LoggingResourceSource(this, tag)
}
