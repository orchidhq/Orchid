package com.eden.orchid.testhelpers

import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.resources.resourcesource.FileResourceSource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource
import com.eden.orchid.api.resources.resourcesource.PluginResourceSource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import org.apache.commons.io.FilenameUtils
import org.json.JSONObject
import java.util.Arrays
import javax.inject.Inject

class TestResourceSource
@Inject
constructor(
    private val mockResources: List<(OrchidContext)->OrchidResource>
) : OrchidResourceSource {

    override val priority: Int
        get() = Integer.MAX_VALUE

    override val scope: OrchidResourceSource.Scope = LocalResourceSource

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        return mockResources
            .map { it(context) }
            .find { it.reference.originalFullFileName == fileName }
    }

    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource> {
        val matchedResoures = if (recursive)
            getResourcesInDirs(context, dirName)
        else
            getResourcesInDir(context, dirName)

        return matchedResoures
            .filter { isValidExtension(it.reference.originalFullFileName, fileExtensions) }
    }

    private fun getResourcesInDir(context: OrchidContext, dirName: String): List<OrchidResource> {
        return mockResources
            .map { it(context) }
            .filter { OrchidUtils.normalizePath(it.reference.originalPath) == OrchidUtils.normalizePath(dirName) }
    }

    private fun getResourcesInDirs(context: OrchidContext, dirName: String): List<OrchidResource> {
        return mockResources
            .map { it(context) }
            .filter { OrchidUtils.normalizePath(it.reference.originalPath).startsWith(OrchidUtils.normalizePath(dirName)) }
    }

    private fun isValidExtension(filename: String, fileExtensions: Array<String>?): Boolean {
        return if (fileExtensions != null)
            Arrays.asList(*fileExtensions).contains(FilenameUtils.getExtension(filename))
        else
            true
    }

    fun toModule(): OrchidModule {
        return TestResourceSourceModule(this)
    }
}

private class TestResourceSourceModule(private val resourceSource: TestResourceSource) : OrchidModule() {
    override fun configure() {
        addToSet(OrchidResourceSource::class.java, resourceSource)
    }
}

class PluginFileResourceSource
@Inject
constructor(
    val pluginClass: Class<out OrchidModule>,
    priority: Int
) : FileResourceSource("", priority, PluginResourceSource) {

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
