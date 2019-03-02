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

@Description("Track changes and create references to all versions of your project.", name = "Changelog")
class ChangelogGenerator
@Inject
constructor(
        context: OrchidContext,
        val model: ChangelogModel
) : OrchidGenerator(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_DEFAULT) {

    companion object {
        const val GENERATOR_KEY = "changelog"
    }

    @Option
    @StringDefault("changelog")
    @Description("The base directory in local resources to look for changelog entries in.")
    lateinit var baseDir: String

    @Option
    @Description("Whether to include minor versions in the version list.")
    var includeMinorVersions: Boolean = false

    @Option
    @StringDefault("{major}.{minor}.{patch}")
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
                true)
                .map { ChangelogVersion(context, format, it.reference.originalFileName, it) }

        var comparator: Comparator<ChangelogVersion>? = null

        if (orderBy.keySet().size == 0) {
            orderBy = JSONObject()
            orderBy.put("major", with(JSONObject()) {
                put("type", "number")
                put("order", 1)
            })
            orderBy.put("minor", with(JSONObject()) {
                put("type", "number")
                put("order", 2)
            })
            orderBy.put("patch", with(JSONObject()) {
                put("type", "number")
                put("order", 3)
            })
        }

        val orderByList = ArrayList<JSONObject>()
        orderBy.keySet()
                .forEach { key ->
                    orderByList.add(with(JSONObject()) {
                        put("key", key)
                        put("type", orderBy.getJSONObject(key).getString("type"))
                        put("order", orderBy.getJSONObject(key).getInt("order"))
                    })
                }

        orderByList
                .sortedBy { it.getInt("order") }
                .forEach { obj ->
                    comparator = if (comparator == null)
                        compareBy { getChangelogComponent(it, obj.getString("key")) }
                    else
                        comparator!!.thenBy { getChangelogComponent(it, obj.getString("key")) }
                }

        versions = versions.sortedWith(comparator!!).reversed()

        model.initialize(versions)

        return emptyList()
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        val versionsJson = JSONArray()
        model.versions.forEach {
            if (it.major) {
                versionsJson.put(it.toJSON())
            }
            else if (it.minor && includeMinorVersions) {
                versionsJson.put(it.toJSON())
            }
        }

        val jsonElement = JSONElement(versionsJson)
        val reference = OrchidReference(context, "meta/versions.json")
        val resource = JsonResource(jsonElement, reference)
        val page = OrchidPage(resource, "changelogVersions", "Changelog Index")
        page.reference.isUsePrettyUrl = false

        context.renderRaw(page)
    }

    fun getChangelogComponent(version: ChangelogVersion, key: String): Comparable<*>? {
        if (orderBy.getJSONObject(key).getString("type").equals("string", true)) {
            return version.versionComponents[key] ?: ""
        }
        else {
            return Integer.parseInt(version.versionComponents[key])
        }
    }
}
