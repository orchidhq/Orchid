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

internal class DefaultFlexibleResourceSource(
    private val delegate: OrchidResourceSource
) : FlexibleResourceSource, OrchidResourceSource by delegate {

    override fun locateResourceEntry(
        context: OrchidContext,
        fileName: String,
        vararg fileExtensions: String
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

    override fun findClosestFile(
        context: OrchidContext,
        filename: String,
        baseDir: String,
        _maxIterations: Int
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
    return DefaultFlexibleResourceSource(this)
}
