package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.ClasspathResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.ArrayList
import java.util.stream.Collectors

/**
 * An OrchidResourceSource that provides resources available on the classpath, using the specified classloader as the
 * root for getting resources.
 */
class ClasspathResourceSource(
    val classloader: ClassLoader,
    override val priority: Int,
    override val scope: OrchidResourceSource.Scope
) : OrchidResourceSource {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        return classloader
            .getResource(OrchidUtils.normalizePath(fileName))
            ?.let {
                ClasspathResource(
                    OrchidReference(context, ClasspathResource.pathFromUrl(it)),
                    it
                )
            }
    }

    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource> {
        TODO()
    }
}
