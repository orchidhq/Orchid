package com.eden.orchid.posts.components

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.posts.model.PostsModel
import com.eden.orchid.posts.pages.PostPage
import com.eden.orchid.utilities.resolve

@Description("Render a list of the most recent blog posts.", name = "Recent Posts")
class RecentPostsComponent : OrchidComponent("recentPosts", 25) {

    val model: PostsModel by lazy {
        context.resolve<PostsModel>()
    }

    @Option
    @IntDefault(10)
    @Description("The maximum number of posts to include in this component.")
    var limit: Int = 10

    @Option
    @Description("Only add latest posts from a specific category.")
    lateinit var category: String

    fun getRecentPosts(): List<PostPage> {
        return if (!EdenUtils.isEmpty(category)) {
            model.getRecentPosts(category, limit)
        } else {
            model.getRecentPosts(null, limit)
        }
    }
}

