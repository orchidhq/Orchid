package com.eden.orchid.api.resources.resource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OrchidFlags
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.asInputStream
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path

/**
 * A Resource type that provides a content to a template from a resource file on disk. When used with
 * renderTemplate() or renderString(), this resource will supply the `page.content` variable to the template renderer as
 * the file contents after having the embedded data removed, and any embedded data will be available in the renderer
 * through the `page` variable. When used with renderRaw(), the raw contents (after having the embedded data removed)
 * will be written directly instead.
 */
class FileResource(
    reference: OrchidReference,
    private val file: File
) : OrchidResource(reference) {

    override fun getContentStream(): InputStream {
        return try {
            FileInputStream(file)
        } catch (e: Exception) {
            e.printStackTrace()
            "".asInputStream()
        }
    }

    override fun canUpdate(): Boolean {
        return true
    }

    override fun canDelete(): Boolean {
        return true
    }

    @Throws(IOException::class)
    override fun update(newContent: InputStream) {
        Files.write(file.toPath(), IOUtils.toByteArray(newContent))
    }

    @Throws(IOException::class)
    override fun delete() {
        file.delete()
    }

    companion object {

        fun pathFromFile(file: File, basePath: Path): String {
            return pathFromFile(
                file,
                basePath.toFile()
            )
        }

        fun pathFromFile(file: File, baseFile: File): String {
            return pathFromFile(
                file,
                baseFile.absolutePath
            )
        }

        fun pathFromFile(
            file: File,
            basePath: String
        ): String {
            var baseFilePath = basePath
            var filePath = file.path
            // normalise Windows-style backslashes to common forward slashes
            baseFilePath = baseFilePath.replace("\\\\".toRegex(), "/")
            filePath = filePath.replace("\\\\".toRegex(), "/")
            // Remove the common base path from the actual file path
            if (filePath.startsWith(baseFilePath)) {
                filePath = filePath.replace(baseFilePath.toRegex(), "")
            }
            if (filePath.startsWith("/")) {
                filePath = filePath.removePrefix("/")
            }
            // if the path is not a child of the base path (i.e. still has relative path segments), strip those away. The
            // resolved "path" of this resource will be the portion after those relative segments.
            filePath = filePath.split("/".toRegex()).toTypedArray()
                .filter { it: String -> !(it == ".." || it == ".") }
                .joinToString("/", "", "", -1, "", null)
            return filePath
        }
    }

}
