package com.eden.orchid.api.resources.resourceSource

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resource.JarResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.utilities.OrchidUtils
import com.google.inject.Provider
import org.apache.commons.io.FilenameUtils
import java.io.IOException
import java.util.ArrayList
import java.util.Objects
import java.util.jar.JarFile

open class JarResourceSource : OrchidResourceSource {

    private val context: Provider<OrchidContext>

    val pluginClass: Class<*>

    override val priority: Int

    constructor(context: Provider<OrchidContext>, priority: Int) {
        this.context = context
        this.pluginClass = this.javaClass
        this.priority = priority
    }

    constructor(context: Provider<OrchidContext>, moduleClass: Class<out OrchidModule>, priority: Int) {
        this.context = context
        this.pluginClass = moduleClass
        this.priority = priority
    }

    private fun jarForClass(): JarFile? {
        val path = "/" + pluginClass.name.replace('.', '/') + ".class"
        val jarUrl = pluginClass.getResource(path) ?: return null

        val url = jarUrl.toString()
        val bang = url.indexOf("!")
        val JAR_URI_PREFIX = "jar:file:"
        return if (url.startsWith(JAR_URI_PREFIX) && bang != -1) {
            try {
                JarFile(url.substring(JAR_URI_PREFIX.length, bang))
            }
            catch (e: IOException) {
                throw IllegalStateException("Error loading jar file.", e)
            }

        }
        else {
            null
        }
    }

    override fun getResourceEntry(fileName: String): OrchidResource? {
        val jarFile = jarForClass()

        if (jarFile != null) {
            val entries = jarFile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()

                if (entry.name.endsWith(fileName) && !entry.isDirectory) {
                    return JarResource(context.get(), jarFile, entry)
                }
            }
        }

        return null
    }

    override fun getResourceEntries(dirName: String, fileExtensions: Array<String>?, recursive: Boolean): List<OrchidResource> {
        val entries = ArrayList<OrchidResource>()

        val jarFile = jarForClass() ?: return entries

        val jarEntries = jarFile.entries()
        while (jarEntries.hasMoreElements()) {
            val jarEntry = jarEntries.nextElement()
            // we are checking a file in the jar
            if (OrchidUtils.normalizePath(jarEntry.name)!!.startsWith("$dirName/") && !jarEntry.isDirectory) {

                if (EdenUtils.isEmpty(fileExtensions) || FilenameUtils.isExtension(jarEntry.name, fileExtensions)) {

                    if (shouldAddEntry(jarEntry.name)) {
                        entries.add(JarResource(context.get(), jarFile, jarEntry))
                    }
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
        return Objects.hash(pluginClass, priority)
    }

    override fun compareTo(other: OrchidResourceSource): Int {
        if (other is JarResourceSource) {
            val superValue = other.priority - priority

            return if (superValue != 0) {
                superValue
            }
            else other.pluginClass.name.compareTo(pluginClass.name)
        }
        else {
            return other.priority - priority
        }
    }
}
