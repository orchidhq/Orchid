package com.eden.orchid.posts.menu

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.model.CategoryModel
import com.eden.orchid.posts.model.PostsModel
import javax.inject.Inject

class LatestPostsMenuType @Inject
constructor(context: OrchidContext, private val postsModel: PostsModel) : OrchidMenuItem(context, "latestPosts", 100) {

    @Option @IntDefault(10)
    var count: Int = 0

    @Option
    lateinit var category: String

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val items = ArrayList<OrchidMenuItemImpl>()

        val categoryModel: CategoryModel?
        val categoryName: String
        val latestPostCount = if (count > 0) count else 10

        if (!EdenUtils.isEmpty(category) && postsModel.categories.containsKey(category)) {
            categoryModel = postsModel.categories[category]
            categoryName = category
        } else {
            categoryModel = postsModel.categories[null]
            categoryName = "blog"
        }

        if (categoryModel != null) {
            items.add(OrchidMenuItemImpl(
                    context,
                    "Latest from " + categoryName,
                    ArrayList<OrchidPage>(categoryModel.first.subList(0, Math.min(categoryModel.first.size, latestPostCount)))))
        }

        return items
    }

}

