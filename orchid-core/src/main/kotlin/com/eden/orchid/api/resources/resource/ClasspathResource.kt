package com.eden.orchid.api.resources.resource

import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.asInputStream
import java.io.InputStream
import java.net.URL

/**
 * A Resource type that provides a content from a resource available on the Classpath. Unlike [JarResource],
 * classpath resources
 */
class ClasspathResource(
    reference: OrchidReference,
    private val classpathResource: URL
) : OrchidResource(reference) {

    override fun getContentStream(): InputStream {
        return try {
            classpathResource.openStream()
        } catch (e: Exception) {
            e.printStackTrace()
            "".asInputStream()
        }
    }

    override fun canUpdate(): Boolean {
        return false
    }

    override fun canDelete(): Boolean {
        return false
    }

    companion object {
        fun pathFromUrl(url: URL): String {
            return url.path
        }
    }
}
