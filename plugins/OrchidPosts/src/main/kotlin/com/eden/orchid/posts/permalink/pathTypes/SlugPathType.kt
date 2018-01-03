package com.eden.orchid.posts.permalink.pathTypes

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.PostsGenerator
import com.eden.orchid.posts.pages.PostArchivePage
import com.eden.orchid.posts.pages.PostPage
import com.eden.orchid.posts.permalink.PermalinkPathType
import com.eden.orchid.posts.utils.PostsUtils
import javax.inject.Inject

class SlugPathType @Inject
constructor() : PermalinkPathType(100) {

    override fun acceptsKey(page: OrchidPage, key: String): Boolean {
        return key == "slug"
    }

    override fun format(page: OrchidPage, key: String): String? {
        if (page is PostPage) {
            val baseCategoryPath: String

            if (EdenUtils.isEmpty(page.category)) {
                baseCategoryPath = "posts"
            } else {
                baseCategoryPath = "posts/" + page.category!!
            }

            val formattedFilename = PostsUtils.getPostFilename(page.resource, baseCategoryPath)

            val matcher = PostsGenerator.pageTitleRegex.matcher(formattedFilename)

            if (matcher.matches()) {
                return matcher.group(4)
            }
        } else if (page is PostArchivePage) {
            return page.category
        }

        return null
    }

}

