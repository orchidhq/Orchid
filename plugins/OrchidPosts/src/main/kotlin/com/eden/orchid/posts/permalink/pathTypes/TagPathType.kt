package com.eden.orchid.posts.permalink.pathTypes

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.pages.PostTagArchivePage
import com.eden.orchid.posts.permalink.PermalinkPathType

import javax.inject.Inject

class TagPathType @Inject
constructor() : PermalinkPathType(100) {

    override fun acceptsKey(page: OrchidPage, key: String): Boolean {
        return key == "tag"
    }

    override fun format(page: OrchidPage, key: String): String? {
        return (page as? PostTagArchivePage)?.tag

    }

}

