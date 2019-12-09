package com.eden.orchid.posts.menu

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.model.CategoryModel
import com.eden.orchid.posts.model.PostsModel

@Description("Latest posts, optionally by category.", name = "Latest Posts")
class LatestPostsMenuType : OrchidMenuFactory("latestPosts") {

    @Option
    @IntDefault(10)
    @Description("The maximum number of posts to include in this menu item.")
    var limit: Int = 10

    @Option
    @Description("Only add latest posts from a specific category.")
    lateinit var category: String

    @Option
    @Description("The title for the root menu item.")
    lateinit var title: String

    @Option
    @Description("Whether to keep the terms as children of a single menu item, or expand them all to the root.")
    var postsAtRoot = false

    override fun getMenuItems(
        context: OrchidContext,
        page: OrchidPage
    ): List<MenuItem> {
        val postsModel = context.resolve(PostsModel::class.java)
        val categoryModel: CategoryModel?

        val items = ArrayList<MenuItem>()
        if (!EdenUtils.isEmpty(category) && postsModel.categories.containsKey(category)) {
            categoryModel = postsModel.categories[category]
        } else {
            categoryModel = postsModel.categories[null]
        }

        val latestPosts = postsModel.getRecentPosts(category, limit)
        if (!EdenUtils.isEmpty(latestPosts)) {
            if(postsAtRoot) {
                latestPosts.forEach {
                    items.add(
                        MenuItem.Builder(context)
                            .page(it)
                            .build()
                    )
                }
            }
            else {
                val title = if (!EdenUtils.isEmpty(this.title)) {
                    this.title
                } else if (!EdenUtils.isEmpty(categoryModel?.title)) {
                    "Latest from " + categoryModel?.title
                } else {
                    "Latest from blog"
                }

                items.add(
                    MenuItem.Builder(context)
                        .title(title)
                        .pages(latestPosts)
                        .build()
                )
            }
        }

        return items
    }

}

