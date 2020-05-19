package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.InlineResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidReference
import java.util.regex.Pattern

/**
 * An OrchidResourceSource that parses the input filename into a String resource.
 */
class InlineResourceSource(
    override val priority: Int,
    override val scope: OrchidResourceSource.Scope
) : OrchidResourceSource {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        val m = inlineFilenamePattern.matcher(fileName)

        if (m.find()) {
            val actualFileName = m.group(2)
            val fileContent = m.group(3)
            return InlineResource(
                StringResource(OrchidReference(context, actualFileName), fileContent)
            )
        }

        return null
    }

    override fun getResourceEntries(context: OrchidContext, dirName: String, fileExtensions: Array<String>?, recursive: Boolean): List<OrchidResource> {
        return emptyList()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InlineResourceSource

        if (priority != other.priority) return false
        if (scope != other.scope) return false

        return true
    }

    private val _hashcode by lazy {
        var result = priority
        result = 31 * result + scope.hashCode()
        result
    }
    override fun hashCode(): Int {
        return _hashcode
    }

    companion object {
        private val inlineFilenamePattern = Pattern.compile("^(inline:(.*?):)(.*)", Pattern.DOTALL)
    }
}


