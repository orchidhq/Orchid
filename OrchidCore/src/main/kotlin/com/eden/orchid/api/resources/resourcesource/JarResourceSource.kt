package com.eden.orchid.api.resources.resourcesource

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.JarResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import org.apache.commons.io.FilenameUtils
import java.io.IOException
import java.util.ArrayList
import java.util.Objects
import java.util.jar.JarFile

open class JarResourceSource(
    private val pluginClass: Class<*>,
    private val jarFile: JarFile,
    override val priority: Int,
    override val scope: OrchidResourceSource.Scope
) : OrchidResourceSource {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        if(fileName.isBlank()) return null

        val entries = jarFile.entries()
        while (entries.hasMoreElements()) {
            val jarEntry = entries.nextElement()

            if (jarEntry.name.endsWith(fileName) && !jarEntry.isDirectory) {
                return JarResource(OrchidReference(context, jarEntry.name), jarFile, jarEntry)
            }
        }

        return null
    }

    override fun getResourceEntries(context: OrchidContext, dirName: String, fileExtensions: Array<String>?, recursive: Boolean): List<OrchidResource> {
        val entries = ArrayList<OrchidResource>()

        val jarEntries = jarFile.entries()
        while (jarEntries.hasMoreElements()) {
            val jarEntry = jarEntries.nextElement()
            // we are checking a file in the jar
            if (OrchidUtils.normalizePath(jarEntry.name)!!.startsWith("$dirName/") && !jarEntry.isDirectory) {

                if (EdenUtils.isEmpty(fileExtensions) || FilenameUtils.isExtension(jarEntry.name, fileExtensions)) {
                    entries.add(JarResource(OrchidReference(context, jarEntry.name), jarFile, jarEntry))
                }
            }
        }

        return entries
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JarResourceSource) return false

        return priority == other.priority && pluginClass == other.pluginClass
    }

    override fun hashCode(): Int {
        return Objects.hash(pluginClass, priority, scope)
    }

    override fun compareTo(other: OrchidResourceSource): Int {
        val superValue = super.compareTo(other)

        return if (superValue != 0) superValue
        else if (other is JarResourceSource) other.pluginClass.name.compareTo(pluginClass.name)
        else superValue
    }

    companion object {
        private const val JAR_URI_PREFIX = "jar:file:"

        fun jarForClass(pluginClass: Class<*>): JarFile {
            val path = "/" + pluginClass.name.replace('.', '/') + ".class"
            val jarUrl = pluginClass.getResource(path) ?: throw IllegalStateException("Could not get jar for class $pluginClass")

            val url = jarUrl.toString()
            val bang = url.indexOf("!")
            return if (url.startsWith(JAR_URI_PREFIX) && bang != -1) {
                try {
                    JarFile(url.substring(JAR_URI_PREFIX.length, bang))
                }
                catch (e: IOException) {
                    throw IllegalStateException("Error loading jar file containing class $pluginClass", e)
                }
            }
            else {
                throw IllegalStateException("Could not get jar for class $pluginClass")
            }
        }
    }
}
