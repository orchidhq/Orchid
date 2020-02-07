package com.eden.orchid.testhelpers

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resourcesource.FileResourceSource
import com.eden.orchid.api.resources.resourcesource.HardcodedResourceSource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource
import com.eden.orchid.api.resources.resourcesource.PluginResourceSource
import com.eden.orchid.utilities.OrchidUtils
import org.apache.commons.io.FilenameUtils
import java.io.File
import java.nio.file.Path
import java.util.Arrays
import javax.inject.Inject

class TestResourceSource(
    mockResources: List<(OrchidContext) -> OrchidResource>
) : OrchidResourceSource by HardcodedResourceSource(
    mockResources,
    Integer.MAX_VALUE,
    LocalResourceSource
) {
    fun toModule(): OrchidModule {
        return TestResourceSourceModule(this)
    }
}

private class TestResourceSourceModule(private val resourceSource: TestResourceSource) : OrchidModule() {
    override fun configure() {
        addToSet(OrchidResourceSource::class.java, resourceSource)
    }
}

class PluginFileResourceSource(
    val pluginClass: Class<out OrchidModule>,
    priority: Int
) : OrchidResourceSource by FileResourceSource(getBasePath(pluginClass), priority, PluginResourceSource) {

    companion object {
        fun getBasePath(pluginClass: Class<out OrchidModule>): Path {
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
}
