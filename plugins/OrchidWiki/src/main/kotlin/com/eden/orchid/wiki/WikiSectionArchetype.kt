package com.eden.orchid.wiki

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionArchetype
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.wiki.pages.WikiPage
import org.json.JSONObject
import javax.inject.Inject

public class WikiSectionArchetype

@Inject
constructor(val context: OrchidContext) : OptionArchetype {

    override fun getOptions(target: Any, archetypeKey: String): JSONObject? {
        if(target !is WikiPage) return null

        if(!EdenUtils.isEmpty(target.section)) {
            val contextOptions = context.query("$archetypeKey.${target.section}")
            if (OrchidUtils.elementIsObject(contextOptions)) {
                return contextOptions!!.element as JSONObject
            }
        }

        return null
    }

}