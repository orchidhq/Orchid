package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.ExternalResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import org.apache.commons.io.FilenameUtils

/**
 * An OrchidResourceSource that provides resources from an external URL. Those resources can later be set to downloaded
 * over HTTP, and the response body is used as the content for that resource.
 */
class CommonExternalAliasesResourceSource(
    override val priority: Int,
    override val scope: OrchidResourceSource.Scope
) : OrchidResourceSource {

    companion object {
        val aliases = mapOf(
            "@jquery.js" to "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js",

            "@bootstrap3.css" to "https://netdna.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css",
            "@bootstrap3.js" to "https://netdna.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js",
            "@bootbox.js" to "https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.3.0/bootbox.min.js",

            "@fontAwesome4.css" to "https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css",
            "@fontAwesome5.js" to "https://use.fontawesome.com/releases/v5.4.0/js/all.js",

            "@githubForkRibbon.css" to "https://cdnjs.cloudflare.com/ajax/libs/github-fork-ribbon-css/0.2.0/gh-fork-ribbon.min.css",
            "@skel.js" to "https://cdnjs.cloudflare.com/ajax/libs/skel/3.0.1/skel.min.js",

            "@uikit.css" to "https://cdnjs.cloudflare.com/ajax/libs/uikit/3.0.0-rc.16/css/uikit.min.css",
            "@uikit.js" to "https://cdnjs.cloudflare.com/ajax/libs/uikit/3.0.0-rc.16/js/uikit.min.js",
            "@uikit-icons.js" to "https://cdnjs.cloudflare.com/ajax/libs/uikit/3.0.0-rc.16/js/uikit-icons.min.js",
            "@vue.js" to "https://cdnjs.cloudflare.com/ajax/libs/vue/2.5.17/vue.min.js",
        )
    }

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        return if (aliases.containsKey(fileName)) {
            val actualFilename = aliases[fileName]!!
            ExternalResource(
                OrchidReference.fromUrl(context, FilenameUtils.getName(actualFilename), actualFilename)
            )
        } else {
            null
        }
    }

    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource> {
        return emptyList()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommonExternalAliasesResourceSource

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
}
