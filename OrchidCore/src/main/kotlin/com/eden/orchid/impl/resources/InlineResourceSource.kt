package com.eden.orchid.impl.resources

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.InlineStringResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import java.util.regex.Pattern

class InlineResourceSource : LocalResourceSource {

    override val priority: Int = Integer.MAX_VALUE - 1

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        val m = inlineFilenamePattern.matcher(fileName)

        if (m.find()) {
            val actualFileName = m.group(2)
            val fileContent = m.group(3)
            return InlineStringResource(context, actualFileName, fileContent)
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


