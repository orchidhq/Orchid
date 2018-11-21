package com.eden.orchid.posts

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionArchetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.posts.pages.PostPage
import org.json.JSONObject
import javax.inject.Inject

@Description("Configure all posts in the same category. Additional configuration values from the same object which " +
        "configures the Posts Generator, at a sub-object for that page's group.",
        name = "Post Category"
)
class PostCategoryArchetype
@Inject
constructor(
        val context: OrchidContext
) : OptionArchetype {

    override fun getOptions(target: Any, archetypeKey: String): Map<String, Any>? {
        if (target !is PostPage) return null

        if (!EdenUtils.isEmpty(target.categoryModel.key)) {
            val contextOptions = context.query("$archetypeKey.${target.categoryModel.key}")
            if (EdenUtils.elementIsObject(contextOptions)) {
                return (contextOptions?.element as? JSONObject)?.toMap()
            }
        }

        return null
    }

}
