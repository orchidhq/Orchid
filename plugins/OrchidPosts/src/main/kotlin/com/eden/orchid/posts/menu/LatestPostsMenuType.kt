package com.eden.orchid.posts.menu

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.model.CategoryModel
import com.eden.orchid.posts.model.PostsModel
import java.util.*
import javax.inject.Inject

class LatestPostsMenuType @Inject
constructor(context: OrchidContext, private val postsModel: PostsModel) : OrchidMenuItem(context, "latestPosts", 100) {

    @Option
    var count: Int = 0

    @Option("category")
    lateinit var categoryNameOption: String

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val items = ArrayList<OrchidMenuItemImpl>()

        val category: CategoryModel
        val categoryName: String
        val latestPostCount = if (count > 0) count else 10

        if (!EdenUtils.isEmpty(categoryNameOption) && postsModel.categories.containsKey(categoryNameOption)) {
            category = postsModel.categories[categoryNameOption]!!
            categoryName = categoryNameOption
        } else {
            category = postsModel.categories[null]!!
            categoryName = "blog"
        }

        items.add(OrchidMenuItemImpl(
                context,
                "Latest from " + categoryName,
                ArrayList<OrchidPage>(category.first.subList(0, Math.min(category.first.size, latestPostCount)))))

        return items
    }

}

