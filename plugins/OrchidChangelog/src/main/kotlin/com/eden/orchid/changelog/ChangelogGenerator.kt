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
import org.json.JSONObject
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

    @Option @StringDefault("{major}.{minor}.{patch}")
    @Description("The format your changelog version follow.")
    lateinit var format: String

    @Option
    @Description("The properties of your format to order by as keys, with their type as values. The values should be " +
            "one of [string, number]"
    )
    lateinit var orderBy: JSONObject

    override fun startIndexing(): List<OrchidPage> {
        var versions = context.getLocalResourceEntries(
                OrchidUtils.normalizePath(baseDir),
                context.compilerExtensions.toTypedArray(),
                false)
                .map { ChangelogVersion(context, format, it.reference.originalFileName, it) }

        var comparator: Comparator<ChangelogVersion>? = null

        if(orderBy.keySet().size == 0) {
            orderBy = JSONObject()
            orderBy.put("major", "number")
            orderBy.put("minor", "number")
            orderBy.put("patch", "number")
        }
        orderBy.keySet().forEach { key ->
            comparator = if (comparator == null)
                compareBy { getChangelogComponent(it, key) }
            else
                comparator!!.thenBy { getChangelogComponent(it, key) }
        }
        versions = versions.sortedWith(comparator!!).reversed()

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

    fun getChangelogComponent(version: ChangelogVersion, key: String): Comparable<*>? {
        if(orderBy.getString(key).equals("string", true)) {
            return version.versionComponents[key] ?: ""
        }
        else {
            return Integer.parseInt(version.versionComponents[key])
        }
    }
}

