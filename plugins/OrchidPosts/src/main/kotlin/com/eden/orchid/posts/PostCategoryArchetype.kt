package com.eden.orchid.posts

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionArchetype
import com.eden.orchid.posts.pages.PostPage
import com.eden.orchid.utilities.OrchidUtils
import org.json.JSONObject
import javax.inject.Inject

public class PostCategoryArchetype

@Inject
constructor(val context: OrchidContext) : OptionArchetype {

    override fun getOptions(target: Any, archetypeKey: String): JSONObject? {
        if(target !is PostPage) return null

        if(!EdenUtils.isEmpty(target.categoryModel.key)) {
            val contextOptions = context.query("$archetypeKey.${target.categoryModel.key}")
            if (OrchidUtils.elementIsObject(contextOptions)) {
                return contextOptions!!.element as JSONObject
            }
        }

        return null
    }

}
