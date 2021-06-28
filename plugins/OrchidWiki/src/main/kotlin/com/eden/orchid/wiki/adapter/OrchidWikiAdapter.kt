package com.eden.orchid.wiki.adapter

import clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.resources.resourcesource.flexible
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.wiki.model.WikiSection
import com.eden.orchid.wiki.pages.WikiPage
import com.eden.orchid.wiki.pages.WikiSummaryPage
import com.eden.orchid.wiki.utils.WikiUtils
import org.apache.commons.io.FilenameUtils
import javax.inject.Inject

class OrchidWikiAdapter
@Inject
constructor(
    val context: OrchidContext
) : WikiAdapter {

    @Option
    @StringDefault("wiki")
    @Description("The base directory in local resources to look for wikis in.")
    lateinit var baseDir: String

    override fun getType(): String = "orchid"

    override fun loadWikiPages(section: WikiSection): Pair<WikiSummaryPage, List<WikiPage>>? {
        val sectionBaseDir = section.sectionBaseDir

        val summary = context
            .getFlexibleResourceSource(LocalResourceSource, null)
            .locateResourceEntry(context, sectionBaseDir + "summary")

        if (summary == null) {
            if (!EdenUtils.isEmpty(section.key)) {
                Clog.w("Could not find wiki summary page in '{}'", sectionBaseDir)
            }
            return null
        }

        return WikiUtils.createWikiFromSummaryFile(context, section, summary) { linkName, linkTarget, _ ->
            val file = sectionBaseDir + linkTarget

            var resource: OrchidResource? = context.getDefaultResourceSource(LocalResourceSource, null).getResourceEntry(context, file)

            if (resource == null) {
                val path = sectionBaseDir + FilenameUtils.removeExtension(linkTarget)
                Clog.w("Could not find wiki resource page at '{}'", file)
                resource = StringResource(
                    OrchidReference(context, "$path/index.md"),
                    linkName
                )
            }

            resource
        }
    }

    private val WikiSection.sectionBaseDir: String
        get() {
            return if (!EdenUtils.isEmpty(key))
                OrchidUtils.normalizePath(baseDir) + "/" + OrchidUtils.normalizePath(key) + "/"
            else
                OrchidUtils.normalizePath(baseDir) + "/"
        }

}
