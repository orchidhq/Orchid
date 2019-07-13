package com.eden.orchid.posts.functions

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.model.PostsModel
import com.eden.orchid.utilities.resolve

@Description("Render a list of the most recent blog posts.", name = "Recent Posts")
class RecentPostsFunction : TemplateFunction("recentPosts", false) {

    @Option
    @IntDefault(10)
    @Description("The maximum number of posts to include in this component.")
    var limit: Int = 10

    @Option
    @Description("Only add latest posts from a specific category.")
    lateinit var category: String

    override fun parameters(): Array<String> {
        return arrayOf("category", "limit")
    }

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        val model: PostsModel = context.resolve()

        return if (!EdenUtils.isEmpty(category)) {
            model.getRecentPosts(category, limit)
        }
        else {
            model.getRecentPosts(null, limit)
        }
    }
}

