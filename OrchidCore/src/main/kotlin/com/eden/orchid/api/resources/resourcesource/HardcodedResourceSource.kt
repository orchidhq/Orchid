package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resource.ClasspathResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import org.apache.commons.io.FilenameUtils
import java.util.ArrayList
import java.util.Arrays
import javax.inject.Inject

/**
 * An OrchidResourceSource that serves from a static list of resources.
 */
class HardcodedResourceSource(
    private val resources: List<(OrchidContext)->OrchidResource>,
    override val priority: Int,
    override val scope: OrchidResourceSource.Scope
) : OrchidResourceSource {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        val normalizedName = OrchidUtils.normalizePath(fileName)
        return resources
            .map { it(context) }
            .find { it.reference.originalFullFileName == normalizedName }
    }

    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource> {
        return resources
            .map { it(context) }
            .filter {
                val cleanPath = OrchidUtils.normalizePath(it.reference.originalPath)
                if (recursive) cleanPath.startsWith(OrchidUtils.normalizePath(dirName))
                else cleanPath == OrchidUtils.normalizePath(dirName)
            }
            .filter {
                if (fileExtensions != null)
                    listOf(*fileExtensions).contains(FilenameUtils.getExtension(it.reference.originalFullFileName))
                else
                    true
            }
    }

}
