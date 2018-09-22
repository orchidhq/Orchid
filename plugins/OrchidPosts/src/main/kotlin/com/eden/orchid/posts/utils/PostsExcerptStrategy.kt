package com.eden.orchid.posts.utils

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.model.PostsModel
import com.eden.orchid.utilities.OrchidUtils
import java.util.regex.Pattern
import javax.inject.Inject

class PostsExcerptStrategy
@Inject
constructor(
        private val postsModel: PostsModel
) {

    fun getExcerpt(page: OrchidPage): String {
        val excerptSeparator = OrchidUtils.normalizePath(postsModel.excerptSeparator)
        val content = page.content

        val pattern = Pattern.compile(excerptSeparator!!, Pattern.DOTALL or Pattern.MULTILINE)

        return if (pattern.matcher(content).find()) {
            pattern.split(content)[0]
        }
        else {
            if (content.length > 240) {
                content.substring(0, 240) + "..."
            }
            else {
                content
            }
        }
    }
}

