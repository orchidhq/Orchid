package com.eden.orchid.api.resources.resource

import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.asInputStream
import java.io.InputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile

/**
 * A Resource type that provides a content to a template from a resource file contained with a jarfile. When used with
 * renderTemplate() or renderString(), this resource will supply the `page.content` variable to the template renderer as
 * the file contents after having the embedded data removed, and any embedded data will be available in the renderer
 * through the `page` variable. When used with renderRaw(), the raw contents (after having the embedded data removed)
 * will be written directly instead.
 */
class JarResource(
    reference: OrchidReference,
    private val jarFile: JarFile,
    private val jarEntry: JarEntry
) : OrchidResource(reference) {

    override fun getContentStream(): InputStream {
        return try {
            jarFile.getInputStream(jarEntry)
        } catch (e: Exception) {
            e.printStackTrace()
            "".asInputStream()
        }
    }
}
