package com.eden.orchid.posts.model

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.posts.pages.PostPage
import com.eden.orchid.utilities.camelCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.titleCase
import com.eden.orchid.utilities.to

class CategoryModel(
        val context: OrchidContext,
        val key: String?,
        val path: String
) : OptionsHolder {

    var first: List<PostPage> = ArrayList()

    @Option @StringDefault(":category/:year/:month/:day/:slug")
    @Description("The permalink structure to use for the blog posts in this category. Permalinks may be " +
            "overridden on any individual post."
    )
    lateinit var permalink: String

    @Option
    @Description("The display title of the category. Defaults to the un-camelCased category key.")
    var title: String = ""
        get() {
            return if(!EdenUtils.isEmpty(field)) {
                field
            }
            else if(!EdenUtils.isEmpty(key)) {
                key!!.from { camelCase() }.to { titleCase() }
            }
            else {
                "Blog"
            }
        }

    val allCategories: Array<String>
        get() {
            return path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        }

}
