package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.utilities.OrchidUtils

class SubdirectoryResourceSource(
    private val delegate: OrchidResourceSource,
    private val subDirectory: String
): OrchidResourceSource by delegate {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        return delegate.getResourceEntry(
            context,
            OrchidUtils.normalizePath("${subDirectory}/$fileName")
        )
    }

    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource> {
        return delegate.getResourceEntries(
            context,
            OrchidUtils.normalizePath("${subDirectory}/$dirName"),
            fileExtensions,
            recursive
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SubdirectoryResourceSource

        if (delegate != other.delegate) return false
        if (subDirectory != other.subDirectory) return false

        return true
    }

    private val _hashcode by lazy {
        var result = delegate.hashCode()
        result = 31 * result + subDirectory.hashCode()
        result
    }
    override fun hashCode(): Int {
        return _hashcode
    }
}

fun OrchidResourceSource.atSubdirectory(subDirectory: String): SubdirectoryResourceSource {
    return SubdirectoryResourceSource(this, subDirectory)
}
