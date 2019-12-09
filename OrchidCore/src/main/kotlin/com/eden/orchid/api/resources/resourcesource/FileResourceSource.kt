package com.eden.orchid.api.resources.resourcesource

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.FileResource
import com.eden.orchid.api.resources.resource.OrchidResource
import org.apache.commons.io.FileUtils
import java.io.File
import java.util.ArrayList

open class FileResourceSource(
    open val directory: String,
    override val priority: Int
) : OrchidResourceSource {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        val baseDirectory = File(directory)
        val file = File("$directory/$fileName")

        return if (file.exists() && !file.isDirectory) {
            FileResource(context, file, baseDirectory)
        }
        else null
    }

    override fun getResourceEntries(context: OrchidContext, dirName: String, fileExtensions: Array<String>?, recursive: Boolean): List<OrchidResource> {
        val entries = ArrayList<OrchidResource>()

        val baseDirectory = File(directory)
        val file = File("$directory/$dirName")

        if (file.exists() && file.isDirectory) {
            val newFiles = FileUtils.listFiles(file, fileExtensions, recursive)

            if (!EdenUtils.isEmpty(newFiles)) {
                for (resourceAsFile in newFiles) {
                    val newFile = resourceAsFile as File
                    if (!isIgnoredFile(context, file) && shouldAddEntry(newFile.name)) {
                        val entry = FileResource(context, newFile, baseDirectory)
                        entry.priority = priority
                        entries.add(entry)
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
        if (other is FileResourceSource) {
            val superValue = other.priority - priority

            return if (superValue != 0) {
                superValue
            }
            else other.directory.compareTo(directory)
        }
        else {
            return other.priority - priority
        }
    }
}
