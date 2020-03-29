package com.eden.orchid.posts.permalink.pathtypes

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.posts.PostsGenerator
import com.eden.orchid.posts.pages.PostPage
import com.eden.orchid.posts.utils.PostsUtils
import javax.inject.Inject

class SlugPathType
@Inject
constructor() : PermalinkPathType() {

    override fun acceptsKey(page: OrchidPage, key: String): Boolean {
        return key == "slug" && page is PostPage
    }

    override fun format(page: OrchidPage, key: String): String? {
        if (page is PostPage) {
            val baseCategoryPath: String

            if (EdenUtils.isEmpty(page.categoryModel.key)) {
                baseCategoryPath = "posts"
            }
            else {
                baseCategoryPath = "posts/" + page.categoryModel.path
            }

            val formattedFilename = PostsUtils.getPostFilename(page.resource, baseCategoryPath)

            val matcher = PostsGenerator.pageTitleRegex.matcher(formattedFilename)

            if (matcher.matches()) {
                return matcher.group(PostsGenerator.Companion.PageTitleGrp.TITLE.ordinal)
            }
        }

        return null
    }

}

