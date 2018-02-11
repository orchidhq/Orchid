package com.eden.orchid.posts.model

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
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

    var first: MutableList<PostPage> = ArrayList()

    @Option
    @StringDefault(":category/:year/:month/:day/:slug")
    lateinit var permalink: String

    @Option
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
