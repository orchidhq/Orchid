package com.eden.orchid.posts.menu

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.model.PostsModel
import java.util.*
import javax.inject.Inject

class TagsMenuType @Inject
constructor(context: OrchidContext, private val postsModel: PostsModel) : OrchidMenuItem(context, "postTags", 100) {

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val items = ArrayList<OrchidMenuItemImpl>()

        if (!EdenUtils.isEmpty(postsModel.tagNames)) {
            val pages = ArrayList<OrchidPage>()
            for ((_, value) in postsModel.tags) {
                pages.add(value.second[0])
            }
            items.add(OrchidMenuItemImpl(context, "Tags", pages))
        }

        return items
    }
}

