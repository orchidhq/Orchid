package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.ExternalResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import org.apache.commons.io.FilenameUtils
import java.io.File

/**
 * An OrchidResourceSource that provides resources from an external URL. Those resources can later be set to downloaded
 * over HTTP, and the response body is used as the content for that resource.
 */
class ExternalResourceSource(
    override val priority: Int,
    override val scope: OrchidResourceSource.Scope
) : OrchidResourceSource {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        return if (OrchidUtils.isExternal(fileName)) {
            ExternalResource(
                OrchidReference.fromUrl(context, FilenameUtils.getName(fileName), fileName)
            )
        }
        else {
            null
        }
    }

    override fun getResourceEntries(context: OrchidContext, dirName: String, fileExtensions: Array<String>?, recursive: Boolean): List<OrchidResource> {
        return emptyList()
    }

    private fun isIgnoredFile(context: OrchidContext, file: File): Boolean {
        return context.ignoredFilenames?.any { file.name == it } ?: false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExternalResourceSource

        if (priority != other.priority) return false
        if (scope != other.scope) return false

        return true
    }

    private val _hashcode by lazy {
        var result = priority
        result = 31 * result + scope.hashCode()
        result
    }
    override fun hashCode(): Int {
        return _hashcode
    }
}
