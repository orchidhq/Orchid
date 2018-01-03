package com.eden.orchid.posts.permalink.pathTypes

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.pages.PostPage
import com.eden.orchid.posts.permalink.PermalinkPathType

import javax.inject.Inject

class MonthPathType @Inject
constructor() : PermalinkPathType(100) {

    override fun acceptsKey(page: OrchidPage, key: String): Boolean {
        return key == "month"
    }

    override fun format(page: OrchidPage, key: String): String? {
        return if (page is PostPage) {
            "" + page.month
        } else null

    }

}

