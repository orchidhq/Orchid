package com.eden.orchid.changelog.model

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
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
    var version: String = versionFilename
        get() {
            return if(!EdenUtils.isEmpty(field)) field else versionFilename
        }

    var content: String = ""
        get() {
            return resource.compileContent(null)
        }

    init {
        extractOptions(context, resource.embeddedData.element as JSONObject)
    }
}

