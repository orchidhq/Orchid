package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.InlineResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidReference
import java.util.regex.Pattern

open class InlineResourceSource(
    override val priority: Int,
    override val scope: OrchidResourceSource.Scope
) : OrchidResourceSource {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        val m = inlineFilenamePattern.matcher(fileName)

        if (m.find()) {
            val actualFileName = m.group(2)
            val fileContent = m.group(3)
            return InlineResource(
                StringResource(OrchidReference(context, actualFileName), fileContent, null)
            )
        }

        return null
    }

    override fun getResourceEntries(context: OrchidContext, dirName: String, fileExtensions: Array<String>?, recursive: Boolean): List<OrchidResource> {
        return emptyList()
    }

    companion object {
        private val inlineFilenamePattern = Pattern.compile("^(inline:(.*?):)(.*)", Pattern.DOTALL)
    }
}


