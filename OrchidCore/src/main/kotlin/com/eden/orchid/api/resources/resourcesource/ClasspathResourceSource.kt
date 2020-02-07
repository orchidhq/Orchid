package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.ClasspathResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import java.util.ArrayList

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
            .getResource(fileName)
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
