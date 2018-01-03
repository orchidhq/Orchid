package com.eden.orchid.posts.permalink.pathTypes

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.pages.PostPage
import com.eden.orchid.posts.permalink.PermalinkPathType

import javax.inject.Inject

class YearPathType @Inject
constructor() : PermalinkPathType(100) {

    override fun acceptsKey(page: OrchidPage, key: String): Boolean {
        return key == "year"
    }

    override fun format(page: OrchidPage, key: String): String? {
        return if (page is PostPage) {
            "" + page.year
        } else null

    }

}

