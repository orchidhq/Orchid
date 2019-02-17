package com.eden.orchid.testhelpers

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.resources.resourcesource.FileResourceSource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource
import com.eden.orchid.api.resources.resourcesource.PluginResourceSource
import com.eden.orchid.utilities.OrchidUtils
import com.google.inject.Provider
import org.apache.commons.io.FilenameUtils
import java.util.Arrays
import javax.inject.Inject

class TestResourceSource
@Inject
constructor(
        private val context: Provider<OrchidContext>,
        private val mockResources: Map<String, Pair<String, Map<String, Any>>>
) : OrchidResourceSource, LocalResourceSource {

    override val priority: Int
        get() = Integer.MAX_VALUE - 2

    override fun getResourceEntry(fileName: String): OrchidResource? {
        return if (mockResources.containsKey(fileName)) {
            StringResource(context.get(), fileName, mockResources[fileName]!!.first, mockResources[fileName]!!.second)
        }
        else null

    }

    override fun getResourceEntries(dirName: String, fileExtensions: Array<String>?, recursive: Boolean): List<OrchidResource> {
        val matchedResoures = if (recursive)
            getResourcesInDirs(dirName)
        else
            getResourcesInDir(dirName)

        return matchedResoures
                .filter { it -> isValidExtension(it.key, fileExtensions) }
                .map { it -> StringResource(context.get(), it.key, it.value.first, it.value.second) }
    }

    private fun getResourcesInDir(dirName: String): List<Map.Entry<String, Pair<String, Map<String, Any>>>> {
        return mockResources
                .entries
                .filter { it -> OrchidUtils.normalizePath(FilenameUtils.getPath(it.key)) == OrchidUtils.normalizePath(dirName) }
    }

    private fun getResourcesInDirs(dirName: String): List<Map.Entry<String, Pair<String, Map<String, Any>>>> {
        return mockResources
                .entries
                .filter { it -> it.key.startsWith(OrchidUtils.normalizePath(dirName)) }
    }

    private fun isValidExtension(filename: String, fileExtensions: Array<String>?): Boolean {
        return if (fileExtensions != null)
            Arrays.asList(*fileExtensions).contains(FilenameUtils.getExtension(filename))
        else
            true
    }

    fun toModule(): OrchidModule {
        return object : OrchidModule() {
            override fun configure() {
                addToSet(LocalResourceSource::class.java, this@TestResourceSource)
            }
        }
    }
}


class PluginFileResourceSource
@Inject
constructor(
        val context: Provider<OrchidContext>,
        val pluginClass: Class<out OrchidModule>,
        priority: Int
) : FileResourceSource(context, "", priority), PluginResourceSource {

    override val directory: String
        get() {
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

            return url
        }
}
