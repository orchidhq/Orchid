package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.FileResource
import com.eden.orchid.api.resources.resource.FileResource.Companion.pathFromFile
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import java.io.File

class FlexibleResourceSource(
    private val delegate: OrchidResourceSource
) : OrchidResourceSource by delegate {

    @JvmOverloads
    fun locateResourceEntry(
        context: OrchidContext,
        fileName: String,
        vararg fileExtensions: String = context.compilerExtensions.toTypedArray()
    ): OrchidResource? {
        val fullFileName = OrchidUtils.normalizePath(fileName)
        return fullFileName
            .takeIf { !it.contains(".") }
            ?.let {
                fileExtensions
                    .asSequence()
                    .mapNotNull { extension -> delegate.getResourceEntry(context, "$fullFileName.$extension") }
                    .firstOrNull()
            }
            ?: delegate.getResourceEntry(context, fullFileName)
    }

    /**
     * This method completely bypasses the normal resource lookup system, and instead directly uses file APIs. This
     * should be avoided, as it cannot be cached and isn't known by the rest of the resource framework.
     * locateResourceEntry() might be a better alternative, but there isn't a direct replacement now. Just try to figure
     * out how to build the plugin without needing this kind of functionality.
     */
    @JvmOverloads
    @Deprecated("Avoid looking up files outside of the resource root. Try to make locateResourceEntry() work instead.")
    fun findClosestFile(
        context: OrchidContext,
        filename: String,
        baseDir: String = context.sourceDir,
        _maxIterations: Int = 10
    ): OrchidResource? {
        var maxIterations: Int = _maxIterations
        var folder = File(baseDir)

        while (true) {
            if (folder.isDirectory) {
                FileUtils
                    .listFiles(folder, null, false)
                    .forEach { file ->
                        if (FilenameUtils.removeExtension(file.name).equals(filename, ignoreCase = true)) {
                            return FileResource(
                                OrchidReference(
                                    context,
                                    pathFromFile(file, context.sourceDir)
                                ),
                                file
                            )
                        }
                    }
            }

            // set the folder to its own parent and search again
            if (folder.parentFile != null && maxIterations > 0) {
                folder = folder.parentFile
                maxIterations--
            } else {
                // there is no more parent to search, exit the loop
                break
            }
        }
        return null
    }

}

fun OrchidResourceSource.flexible(): FlexibleResourceSource {
    return FlexibleResourceSource(this)
}
