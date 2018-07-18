package com.eden.orchid.pages

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionArchetype
import com.eden.orchid.pages.pages.StaticPage
import org.json.JSONObject
import javax.inject.Inject

public class PageGroupArchetype

@Inject
constructor(val context: OrchidContext) : OptionArchetype {

    override fun getOptions(target: Any, archetypeKey: String): Map<String, Any>? {
        if(target !is StaticPage) return null

        if(!EdenUtils.isEmpty(target.group)) {
            val contextOptions = context.query("$archetypeKey.${target.group}")
            if (EdenUtils.elementIsObject(contextOptions)) {
                return (contextOptions?.element as? JSONObject)?.toMap()
            }
        }

        return null
    }

}
