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
        context: OrchidContext,
        val versionFormat: String,
        val versionFilename: String,
        val resource: OrchidResource
) : OptionsHolder
{

    @Option
    @Description("The name of the version")
    var version: String = versionFilename
        get() {
            return if(!EdenUtils.isEmpty(field)) field else versionFilename
        }

    @Option
    @Description("Whether this is a major version.")
    var major: Boolean = false

    @Option
    @Description("Whether this is a minor version.")
    var minor: Boolean = false

    @Option
    @Description("The URL that hosts the documentation for this specific version")
    lateinit var url: String

    @AllOptions
    lateinit var allData: Map<String, Any>

    val content: String
        get() {
            return resource.compileContent(null)
        }

    val versionComponents = LinkedHashMap<String, String>()

    init {
        extractOptions(context, (resource.embeddedData.element as JSONObject).toMap())

        val versionFormatRegex = versionFormat.replace(".", "\\.").replace("\\{\\w+?\\}".toRegex(), "(\\\\w+)").toRegex()
        val match = versionFormatRegex.find(version)
        if(match != null) {
            val componentKeys = ArrayList<String>()
            "\\{(.*?)\\}".toRegex().findAll(versionFormat).forEach {
                componentKeys.add(it.groupValues[1])
            }

            match.groupValues.forEachIndexed { index, value ->
                if(index > 0) {
                    versionComponents[componentKeys[index-1]] = value
                }
            }
        }
    }

    fun toJSON(): JSONObject {
        val json = JSONObject()
        json.put("version", this.version)
        json.put("major", this.major)
        json.put("minor", this.minor)
        json.put("url", this.url)
        json.put("components", JSONObject(versionComponents))

        return json
    }

// Map Implementation
//----------------------------------------------------------------------------------------------------------------------

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

