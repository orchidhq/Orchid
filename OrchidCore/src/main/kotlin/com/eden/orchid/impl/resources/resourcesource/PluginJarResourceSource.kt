package com.eden.orchid.impl.resources.resourcesource

import com.eden.orchid.api.resources.resourcesource.FileResourceSource
import com.eden.orchid.api.resources.resourcesource.JarResourceSource
import com.eden.orchid.api.resources.resourcesource.JarResourceSource.Companion.jarForClass
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource
import com.eden.orchid.api.resources.resourcesource.PluginResourceSource
import java.io.File
import java.nio.file.Path

object PluginJarResourceSource {
    fun create(
        pluginClass: Class<*>,
        priority: Int,
        scope: OrchidResourceSource.Scope = PluginResourceSource
    ): OrchidResourceSource {
        return try {
            JarResourceSource(jarForClass(pluginClass), priority, scope)
        } catch (e: Exception) {
            FileResourceSource(getBasePath(pluginClass), priority, scope)
        }
    }

    fun getBasePath(pluginClass: Class<*>): Path {
        val path = "/" + pluginClass.name.replace('.', '/') + ".class"
        val jarUrl = pluginClass.getResource(path)

        var url = jarUrl?.toString() ?: ""

        // initial path
        url = url.reversed()
        url = url.replaceFirst(path.reversed(), "")

        // intellij paths
        url = url.replaceFirst("/out/production/classes".reversed(), "")
        url = url.replaceFirst("/out/test/classes".reversed(), "")

        // gradle paths
        url = url.replaceFirst("/build/classes/java/main".reversed(), "")
        url = url.replaceFirst("/build/classes/java/test".reversed(), "")
        url = url.replaceFirst("/build/classes/kotlin/main".reversed(), "")
        url = url.replaceFirst("/build/classes/kotlin/test".reversed(), "")

        // cleanup path
        url = url.reversed()
        url = url.replaceFirst("file:", "")
        url += "/src/main/resources"

        return File(url).toPath()
    }
}
