package com.eden.orchid.posts.menu

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.model.PostsModel
import java.util.*
import javax.inject.Inject

class CategoriesMenuType @Inject
constructor(context: OrchidContext, private val postsModel: PostsModel) : OrchidMenuItem(context, "postCategories", 100) {

    @Option
    lateinit var category: String

    @Option
    lateinit var title: String

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val items = ArrayList<OrchidMenuItemImpl>()

        if (!EdenUtils.isEmpty(category) && postsModel.categories.containsKey(category)) {
            val categoryDef = postsModel.categories[category]
            if (!EdenUtils.isEmpty(categoryDef!!.second)) {
                val menuItem = OrchidMenuItemImpl(context, categoryDef.second[0])
                if (!EdenUtils.isEmpty(title)) {
                    menuItem.title = title
                }
                items.add(menuItem)
            }
        } else if (postsModel.categoryNames.size > 1) {
            val pages = ArrayList<OrchidPage>()
            for ((_, value) in postsModel.categories) {
                if (!EdenUtils.isEmpty(value.second)) {
                    pages.add(value.second[0])
                }
            }

            val menuItem = OrchidMenuItemImpl(context, "Categories", pages)
            if (!EdenUtils.isEmpty(title)) {
                menuItem.title = title
            }
            items.add(menuItem)
        } else if (postsModel.categories.containsKey(null)) {
            val categoryDef = postsModel.categories[null]
            if (!EdenUtils.isEmpty(categoryDef!!.second)) {
                val menuItem = OrchidMenuItemImpl(context, categoryDef.second[0])
                if (!EdenUtils.isEmpty(title)) {
                    menuItem.title = title
                }
                items.add(menuItem)
            }
        }

        return items
    }
}

