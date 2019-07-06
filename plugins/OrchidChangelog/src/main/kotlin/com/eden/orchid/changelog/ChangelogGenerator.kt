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
import javax.inject.Inject

@Description("Track changes and create references to all versions of your project.", name = "Changelog")
class ChangelogGenerator : OrchidGenerator<ChangelogModel>(GENERATOR_KEY, PRIORITY_DEFAULT) {

    companion object {
        const val GENERATOR_KEY = "changelog"
    }

    @Option
    @StringDefault("changelog")
    @Description("The base directory in local resources to look for changelog entries in.")
    lateinit var baseDir: String

    @Option
    @Description("Whether to include minor versions in the versions.json file.")
    var includeMinorVersions: Boolean = false

    @Option
    @Description("Whether to include release notes for each entry in the versions.json file.")
    var includeReleaseNotes: Boolean = false

    @Option
    @StringDefault("{major}.{minor}.{patch}")
    @Description("The format your changelog version follow.")
    lateinit var format: String

    @Option
    @Description(
        "The properties of your format to order by as keys, with their type as values. The values should be " +
                "one of [string, number]"
    )
    lateinit var orderBy: JSONObject

    override fun startIndexing(context: OrchidContext): ChangelogModel {
        var versions = context.getLocalResourceEntries(
            OrchidUtils.normalizePath(baseDir),
            context.compilerExtensions.toTypedArray(),
            true
        )
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

        // order versions
        orderByList
            .sortedBy { it.getInt("order") }
            .forEach { obj ->
                comparator = if (comparator == null)
                    compareBy { it.getChangelogComponent(obj.getString("key")) }
                else
                    comparator!!.thenBy { it.getChangelogComponent(obj.getString("key")) }
            }

        versions = versions.sortedWith(comparator!!).reversed()

        // determine which version components got bumped. Iterate list in reverse, since original list has the newest
        //     version first. We need to detect version bumps starting with the oldest version.
        var lastVersion: ChangelogVersion? = null
        versions.asReversed().forEach {
            it.checkBump(lastVersion)
            lastVersion = it
        }

        return ChangelogModel(versions)
    }

    override fun startGeneration(context: OrchidContext, model: ChangelogModel) {
        val versionsJson = JSONArray()
        model.versions.forEach {
            if (it.major || (it.minor && includeMinorVersions)) {
                versionsJson.put(it.toJSON(includeReleaseNotes))
            }
        }

        val jsonElement = JSONElement(versionsJson)
        val reference = OrchidReference(context, "meta/versions.json")
        val resource = JsonResource(jsonElement, reference)
        val page = OrchidPage(resource, "changelogVersions", "Changelog Index")
        page.reference.isUsePrettyUrl = false

        context.renderRaw(page)
    }

    private val ChangelogVersion.major: Boolean
        get() {
            return versionComponents["major"]?.second != null && versionComponents["major"]!!.second
        }

    private val ChangelogVersion.minor: Boolean
        get() {
            return versionComponents["minor"]?.second != null && versionComponents["minor"]!!.second
        }

    private fun ChangelogVersion.getChangelogComponent(key: String): Comparable<*>? {
        if (orderBy.getJSONObject(key).getString("type").equals("string", true)) {
            return versionComponents[key]?.first ?: ""
        } else {
            return Integer.parseInt(versionComponents[key]?.first ?: "0")
        }
    }

    private fun ChangelogVersion.checkBump(previous: ChangelogVersion?) {
        for(componentKey in versionComponents.keys) {
            val shouldBump = if(previous == null) {
                true
            }
            else if(versionComponents[componentKey]?.first != previous.versionComponents[componentKey]?.first) {
                true
            }
            else {
                false
            }

            if(shouldBump) {
                versionComponents[componentKey] = versionComponents[componentKey]!!.copy(second = true)
            }
        }
    }
}
