package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.ExternalResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import org.apache.commons.io.FilenameUtils
import java.io.File

open class ExternalResourceSource(
    override val priority: Int
) : OrchidResourceSource {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        return if (OrchidUtils.isExternal(fileName)) {
            val ref = OrchidReference.fromUrl(context, FilenameUtils.getName(fileName), fileName)
            val resource = ExternalResource(ref)

            if(context.isProduction) {
                resource.isDownload = true
            }

            resource
        }
        else {
            null
        }
    }

    override fun getResourceEntries(context: OrchidContext, dirName: String, fileExtensions: Array<String>?, recursive: Boolean): List<OrchidResource> {
        return emptyList()
    }

    private fun isIgnoredFile(context: OrchidContext, file: File): Boolean {
        return context.ignoredFilenames?.any { file.name == it } ?: false
    }
}
