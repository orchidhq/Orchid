package com.eden.orchid.changelog.model

import com.eden.common.json.JSONElement
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.AllOptions
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.changelog.ChangelogGenerator
import org.json.JSONObject

@Archetype(value = ConfigArchetype::class, key = "${ChangelogGenerator.GENERATOR_KEY}.allVersions")
class ChangelogVersion(
    private val context: OrchidContext,
    private val versionFormat: String,
    private val versionName: String,
    private val versionReleaseDate: String?,
    private val resource: OrchidResource
) : OptionsHolder {

    @Option
    @Description("The name of the version")
    var version: String = versionName
        get() {
            return if (!EdenUtils.isEmpty(field)) field else versionName
        }

    @Option
    @Description("The release date of this version")
    var releaseDate: String? = versionReleaseDate
        get() {
            return if (!EdenUtils.isEmpty(field)) field else versionReleaseDate
        }

    @Option
    @Description("The URL that hosts the documentation for this specific version")
    lateinit var url: String

    @AllOptions
    lateinit var allData: Map<String, Any>

    val content: String
        get() {
            return resource.compileContent(context, null)
        }

    val versionComponents = LinkedHashMap<String, Pair<String, Boolean>>()

    init {
        extractOptions(context, resource.embeddedData)

        val versionFormatRegex =
            versionFormat.replace(".", "\\.").replace("\\{\\w+?\\}".toRegex(), "(\\\\w+)").toRegex()
        val match = versionFormatRegex.find(version)
        if (match != null) {
            val componentKeys = ArrayList<String>()
            "\\{(.*?)\\}".toRegex().findAll(versionFormat).forEach {
                componentKeys.add(it.groupValues[1])
            }

            match.groupValues.forEachIndexed { index, value ->
                if (index > 0) {
                    versionComponents[componentKeys[index - 1]] = value to false
                }
            }
        }
    }

    fun toJSON(includeReleaseNotes: Boolean): JSONObject {
        val json = JSONObject()
        json.put("version", this.version)
        json.put("url", this.url)
        versionComponents.forEach { key, value ->
            json.put(key, value.second)
        }
        json.put("components", JSONObject(versionComponents.mapValues { it.value.first }))

        if (includeReleaseNotes) {
            json.put("releaseNotes", this.content)
        }

        return json
    }

// Map Implementation
// ---------------------------------------------------------------------------------------------------------------------

    fun has(key: String): Boolean {
        return allData.containsKey(key)
    }

    operator fun get(key: String): Any? {
        return allData[key]
    }

    fun query(key: String): Any? {
        return JSONElement(JSONObject(allData)).query(key)?.element
    }
}
