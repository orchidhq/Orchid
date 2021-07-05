package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.ClasspathResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ClasspathResourceSource

        if (classloader != other.classloader) return false
        if (priority != other.priority) return false
        if (scope != other.scope) return false

        return true
    }

    private val _hashcode by lazy {
        var result = classloader.hashCode()
        result = 31 * result + priority
        result = 31 * result + scope.hashCode()
        result
    }
    override fun hashCode(): Int {
        return _hashcode
    }
}
