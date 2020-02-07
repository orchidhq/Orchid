package com.eden.orchid.api.resources.resourcesource

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.FileResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.Path
import java.util.ArrayList

open class FileResourceSource(
    private val baseDirectory: Path,
    override val priority: Int,
    override val scope: OrchidResourceSource.Scope
) : OrchidResourceSource {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        val file = baseDirectory.resolve(fileName).toFile()

        return if (file.exists() && !file.isDirectory)
            FileResource(
                OrchidReference(
                    context,
                    FileResource.pathFromFile(file, baseDirectory)
                ),
                file
            ) else null
    }

    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource> {
        val entries = ArrayList<OrchidResource>()

        val file = baseDirectory.resolve(dirName).toFile()

        if (file.exists() && file.isDirectory) {
            val newFiles = FileUtils.listFiles(file, fileExtensions, recursive)

            if (!EdenUtils.isEmpty(newFiles)) {
                for (resourceAsFile in newFiles) {
                    val newFile = resourceAsFile as File
                    if (!isIgnoredFile(context, file)) {
                        entries.add(
                            FileResource(
                                OrchidReference(
                                    context,
                                    FileResource.pathFromFile(newFile, baseDirectory)
                                ),
                                newFile
                            )
                        )
                    }
                }
            }
        }

        return entries
    }

    private fun isIgnoredFile(context: OrchidContext, file: File): Boolean {
        return context.ignoredFilenames?.any { file.name == it } ?: false
    }

    override fun compareTo(other: OrchidResourceSource): Int {
        val superValue = super.compareTo(other)
        return if (superValue != 0) superValue
        else if (other is FileResourceSource) other.baseDirectory.compareTo(baseDirectory)
        else superValue
    }
}
