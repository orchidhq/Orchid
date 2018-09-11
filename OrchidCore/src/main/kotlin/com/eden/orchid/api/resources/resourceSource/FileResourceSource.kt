package com.eden.orchid.api.resources.resourceSource

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.FileResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.google.inject.Provider
import org.apache.commons.io.FileUtils

import java.io.File
import java.util.ArrayList

open class FileResourceSource : OrchidResourceSource {

    private val context: Provider<OrchidContext>

    val directory: String

    override val priority: Int

    constructor(context: Provider<OrchidContext>, priority: Int) {
        this.context = context
        this.directory = ""
        this.priority = priority
    }

    constructor(context: Provider<OrchidContext>, directory: String, priority: Int) {
        this.context = context
        this.directory = directory
        this.priority = priority
    }

    override fun getResourceEntry(fileName: String): OrchidResource? {
        val file = File("$directory/$fileName")

        return if (file.exists() && !file.isDirectory) {
            FileResource(context.get(), file)
        }
        else null

    }

    override fun getResourceEntries(dirName: String, fileExtensions: Array<String>?, recursive: Boolean): List<OrchidResource> {
        val entries = ArrayList<OrchidResource>()

        val fullPath = "$directory/$dirName"
        val file = File(fullPath)

        if (file.exists() && file.isDirectory) {
            val newFiles = FileUtils.listFiles(file, fileExtensions, recursive)

            if (!EdenUtils.isEmpty(newFiles)) {
                for (`object` in newFiles) {
                    val newFile = `object` as File
                    if (shouldAddEntry(newFile.name)) {
                        val entry = FileResource(context.get(), newFile)
                        entry.priority = priority
                        entries.add(entry)
                    }
                }
            }
        }

        return entries
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
