package com.eden.orchid.posts.permalink.pathTypes

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.posts.pages.PostPage
import javax.inject.Inject

class CategoryPathType @Inject
constructor() : PermalinkPathType(100) {

    override fun acceptsKey(page: OrchidPage, key: String): Boolean {
        return key == "category" && page is PostPage
    }

    override fun format(page: OrchidPage, key: String): String? {
        if (page is PostPage) {
            return if(page.categoryModel.key != null) page.categoryModel.path else ""
        }

        return null
    }

}

