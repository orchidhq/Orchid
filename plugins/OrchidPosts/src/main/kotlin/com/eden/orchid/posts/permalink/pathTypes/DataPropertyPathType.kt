package com.eden.orchid.posts.permalink.pathTypes

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.permalink.PermalinkPathType

import javax.inject.Inject

class DataPropertyPathType @Inject
constructor() : PermalinkPathType(1) {

    override fun acceptsKey(page: OrchidPage, key: String): Boolean {
        return page.data.has(key)
    }

    override fun format(page: OrchidPage, key: String): String? {
        return page.data.get(key).toString()
    }

}

