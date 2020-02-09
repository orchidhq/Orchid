package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.JarResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import org.apache.commons.io.FilenameUtils
import java.io.IOException
import java.util.ArrayList
import java.util.jar.JarFile

/**
 * An OrchidResourceSource that serves resources as entries from a JarFile. The JarFile is typically looked up as the
 * JarFile containing a particular classfile.
 */
class JarResourceSource(
    private val jarFile: JarFile,
    override val priority: Int,
    override val scope: OrchidResourceSource.Scope
) : OrchidResourceSource {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        if (fileName.isBlank()) return null

        val normalizedName = OrchidUtils.normalizePath(fileName)

        val entries = jarFile.entries()
        while (entries.hasMoreElements()) {
            val jarEntry = entries.nextElement()

            if (jarEntry.name.endsWith(normalizedName) && !jarEntry.isDirectory) {
                return JarResource(OrchidReference(context, jarEntry.name), jarFile, jarEntry)
            }
        }

        return null
    }

    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource> {
        val entries = ArrayList<OrchidResource>()

        val normalizedName = OrchidUtils.normalizePath(dirName)

        val jarEntries = jarFile.entries()
        while (jarEntries.hasMoreElements()) {
            val jarEntry = jarEntries.nextElement()

            if (jarEntry.isDirectory) continue

            val normalizedEntryName = OrchidUtils.normalizePath(jarEntry.name)
            val path = OrchidUtils.normalizePath(FilenameUtils.getPath(normalizedEntryName))

            if (recursive) {
                if (!path.startsWith(normalizedName)) continue
            } else {
                if (path != normalizedName) continue
            }

            if (normalizedEntryName.startsWith("META-INF")) continue

            if (fileExtensions.isNullOrEmpty() || FilenameUtils.isExtension(normalizedEntryName, fileExtensions)) {
                entries.add(JarResource(OrchidReference(context, normalizedEntryName), jarFile, jarEntry))
            }
        }

        return entries
    }

    companion object {
        private const val JAR_URI_PREFIX = "jar:file:"

        fun jarForClass(pluginClass: Class<*>): JarFile {
            val path = "/" + pluginClass.name.replace('.', '/') + ".class"
            val jarUrl =
                pluginClass.getResource(path) ?: throw IllegalStateException("Could not get jar for class $pluginClass")

            val url = jarUrl.toString()
            val bang = url.indexOf("!")
            return if (url.startsWith(JAR_URI_PREFIX) && bang != -1) {
                try {
                    JarFile(url.substring(JAR_URI_PREFIX.length, bang))
                } catch (e: IOException) {
                    throw IllegalStateException("Error loading jar file containing class $pluginClass", e)
                }
            } else {
                throw IllegalStateException("Could not get jar for class $pluginClass")
            }
        }
    }
}
