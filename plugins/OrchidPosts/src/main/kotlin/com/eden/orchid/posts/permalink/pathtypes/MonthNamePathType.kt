package com.eden.orchid.posts.permalink.pathtypes

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.posts.pages.PostPage
import java.time.Month
import javax.inject.Inject

class MonthNamePathType
@Inject
constructor() : PermalinkPathType(100) {

    override fun acceptsKey(page: OrchidPage, key: String): Boolean {
        return key == "monthName"
    }

    override fun format(page: OrchidPage, key: String): String? {
        return if (page is PostPage) {
            Month.of(page.month).toString()
        } else page.publishDate.month.toString()

    }

}

