package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.AbstractTheme
import com.eden.orchid.utilities.OrchidUtils

internal class DefaultTemplateResourceSource(
    private val delegate: OrchidResourceSource,
    private val theme: AbstractTheme
) : OrchidResourceSource by delegate, TemplateResourceSource {

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        return getResourceEntry(context,null, fileName.split(","))
    }

    override fun getResourceEntry(context: OrchidContext, templateSubDir: String?, possibleFileNames: List<String>): OrchidResource? {
        return sequence {
            val themePreferredExtension = theme.preferredTemplateExtension
            val defaultExtension = context.defaultTemplateExtension

            for(nullableTemplateName in possibleFileNames) {
                val template = nullableTemplateName.removePrefix("?")

                // look for exact template name
                yield(template)
                yield("$template.$themePreferredExtension") // get rid of this?
                yield("$template.$defaultExtension") // get rid of this?

                // if we have a subdirectory, look there too
                if(!templateSubDir.isNullOrBlank()) {
                    yield("$templateSubDir/$template")
                    yield("$templateSubDir/$template.$themePreferredExtension")
                    yield("$templateSubDir/$template.$defaultExtension")
                }
            }
        }
            .filter { it.isNotBlank() }
            .distinct()
            .mapNotNull { delegate.getResourceEntry(context, it) }
            .firstOrNull()
    }

    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource> {
        throw NotImplementedError("TemplateResourceSource can only look up a single resource entry")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DefaultTemplateResourceSource

        if (theme != other.theme) return false
        if (delegate != other.delegate) return false

        return true
    }

    private val _hashcode by lazy {
        var result = theme.hashCode()
        result = 31 * result + delegate.hashCode()
        result
    }
    override fun hashCode(): Int {
        return _hashcode
    }
}

@JvmOverloads
fun OrchidResourceSource.useForTemplates(
    theme: AbstractTheme,
    templateBaseDir: String = "templates"
): TemplateResourceSource {
    return DefaultTemplateResourceSource(this.atSubdirectory(templateBaseDir), theme)
}
