package com.eden.orchid.posts.permalink.pathTypes

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.pages.PostArchivePage
import com.eden.orchid.posts.permalink.PermalinkPathType

import javax.inject.Inject

class ArchiveIndexPathType @Inject
constructor() : PermalinkPathType(100) {

    override fun acceptsKey(page: OrchidPage, key: String): Boolean {
        return key == "archiveIndex"
    }

    override fun format(page: OrchidPage, key: String): String? {
        if (page is PostArchivePage) {

            val index = page.index

            return if (index == 1) "" else "" + index
        }

        return null
    }

}

