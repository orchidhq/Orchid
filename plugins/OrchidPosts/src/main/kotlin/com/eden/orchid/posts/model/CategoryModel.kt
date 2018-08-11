package com.eden.orchid.posts.model

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.posts.pages.PostPage
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.camelCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.titleCase
import com.eden.orchid.utilities.to
import javax.inject.Inject


class CategoryModel
@Inject
constructor(val context: OrchidContext) : OptionsHolder {

    var first: List<PostPage> = ArrayList()

    @Option
    var key: String? = null

    var path: String = ""

    @Option @StringDefault(":category/:year/:month/:day/:slug")
    @Description("The permalink structure to use for the blog posts in this category. Permalinks may be " +
            "overridden on any individual post."
    )
    lateinit var permalink: String

    @Option
    @Description("The display title of the category. Defaults to the un-camelCased category key.")
    lateinit var title: String

    lateinit var allCategories: Array<String>

    override fun onPostExtraction() {
        key = OrchidUtils.normalizePath(key)

        if(!EdenUtils.isEmpty(key)) {
            val categoryPath = key!!.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            key = categoryPath.last()
            path = OrchidUtils.normalizePath(categoryPath.joinToString("/"))
        }
        else {
            key = null
            path = ""
        }
        allCategories = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        title = if(!EdenUtils.isEmpty(title)) {
            title
        }
        else if(!EdenUtils.isEmpty(key)) {
            key!!.from { camelCase() } to { titleCase() }
        }
        else {
            "Blog"
        }
    }



}
