package com.eden.orchid.posts.permalink.pathtypes

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.posts.pages.AuthorPage
import javax.inject.Inject

class AuthorNamePathType @Inject
constructor() : PermalinkPathType(100) {

    override fun acceptsKey(page: OrchidPage, key: String): Boolean {
        return key == "authorName" && page is AuthorPage
    }

    override fun format(page: OrchidPage, key: String): String? {
        return (page as AuthorPage).author.name
    }

}
