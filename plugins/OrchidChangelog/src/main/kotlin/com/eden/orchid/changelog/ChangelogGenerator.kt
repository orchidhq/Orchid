package com.eden.orchid.changelog

import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.JsonResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.changelog.model.ChangelogModel
import com.eden.orchid.changelog.model.ChangelogVersion
import com.eden.orchid.utilities.OrchidUtils
import org.json.JSONArray
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Description("Track changes and create references to all versions of your project.")
class ChangelogGenerator @Inject
constructor(context: OrchidContext, val model: ChangelogModel) : OrchidGenerator(context, "changelog", OrchidGenerator.PRIORITY_DEFAULT) {

    @Option @StringDefault("changelog")
    @Description("The base directory in local resources to look for changelog entries in.")
    lateinit var baseDir: String

    @Option
    @Description("Whether to include minor versions in the version list.")
    var includeMinorVersions: Boolean = false

    override fun startIndexing(): List<OrchidPage> {
        val versions = context.getLocalResourceEntries(
                OrchidUtils.normalizePath(baseDir),
                context.compilerExtensions.toTypedArray(),
                false)
                .map { ChangelogVersion(context, it.reference.originalFileName, it) }
                .sortedBy { it.version }
                .reversed()

        model.initialize(versions)

        val versionsJson = JSONArray()

        versions.forEach {
            if(it.major) {
                versionsJson.put(it.toJSON())
            }
            else if(it.minor && includeMinorVersions) {
                versionsJson.put(it.toJSON())
            }
        }

        val jsonElement = JSONElement(versionsJson)
        val reference = OrchidReference(context, "meta/versions.json")
        val resource = JsonResource(jsonElement, reference)
        val page = OrchidPage(resource, "changelogVersions")
        page.reference.isUsePrettyUrl = false

        return listOf(page)
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach { context.renderRaw(it) }

    }
}

