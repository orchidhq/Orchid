package com.eden.orchid.pages

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionArchetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.pages.pages.StaticPage
import org.json.JSONObject
import javax.inject.Inject

@Description(
    "Configure all pages in the same page group. Additional configuration values from the same object which " +
        "configures the Pages Generator, at a sub-object for that page's group.",
    name = "Page Group"
)
class PageGroupArchetype
@Inject
constructor(
    val context: OrchidContext
) : OptionArchetype {

    override fun getOptions(target: Any, archetypeKey: String): Map<String, Any>? {
        if (target !is StaticPage) return null

        if (!EdenUtils.isEmpty(target.group)) {
            val contextOptions = context.query("$archetypeKey.${target.group}")
            if (EdenUtils.elementIsObject(contextOptions)) {
                return (contextOptions?.element as? JSONObject)?.toMap()
            }
        }

        return null
    }
}
