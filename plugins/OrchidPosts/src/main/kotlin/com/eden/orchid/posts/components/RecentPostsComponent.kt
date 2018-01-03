package com.eden.orchid.posts.components

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.posts.model.PostsModel
import com.eden.orchid.posts.pages.PostPage

import javax.inject.Inject

class RecentPostsComponent @Inject
constructor(context: OrchidContext, var postsModel: PostsModel) : OrchidComponent(context, "recentPosts", 25) {

    @Option
    lateinit var category: String

    @Option @IntDefault(10)
    var limit: Int = 10

    fun getRecentPosts(): List<PostPage> {
        return if (!EdenUtils.isEmpty(category)) {
            postsModel.getRecentPosts(category, limit)
        } else {
            postsModel.getRecentPosts(null, limit)
        }
    }
}

