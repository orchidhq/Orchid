package com.eden.orchid.api.resources.resourcesource

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.FileResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import org.apache.commons.io.FileUtils
import java.io.File
import java.util.ArrayList

class SubdirectoryResourceSource(
    private val delegate: OrchidResourceSource,
    private val subDirectory: String
): OrchidResourceSource by delegate {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        return delegate.getResourceEntry(
            context,
            OrchidUtils.normalizePath("${subDirectory}/$fileName")
        )
    }

    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource> {
        return delegate.getResourceEntries(
            context,
            OrchidUtils.normalizePath("${subDirectory}/$dirName"),
            fileExtensions,
            recursive
        )
    }

}
