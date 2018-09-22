package com.eden.orchid.posts.functions

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.posts.model.PostsModel
import javax.inject.Inject

@Description("Render a list of the most recent blog posts.", name = "Recent Posts")
class RecentPostsFunction
@Inject
constructor(
        var postsModel: PostsModel
) : TemplateFunction("recentPosts", false) {

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

    override fun apply(): Any {
        return if (!EdenUtils.isEmpty(category)) {
            postsModel.getRecentPosts(category, limit)
        }
        else {
            postsModel.getRecentPosts(null, limit)
        }
    }
}

