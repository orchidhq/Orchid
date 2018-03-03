package com.eden.orchid.changelog.model

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.OrchidResource
import org.json.JSONObject

class ChangelogVersion(
        context: OrchidContext,
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

    var content: String = ""
        get() {
            return resource.compileContent(null)
        }

    init {
        extractOptions(context, resource.embeddedData.element as JSONObject)
    }

    fun toJSON(): JSONObject {
        val json = JSONObject()
        json.put("version", this.version)
        json.put("major", this.major)
        json.put("minor", this.minor)
        json.put("url", this.url)

        return json
    }

}

