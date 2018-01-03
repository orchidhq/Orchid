package com.eden.orchid.posts.permalink

import com.eden.orchid.api.registration.Prioritized
import com.eden.orchid.api.theme.pages.OrchidPage

abstract class PermalinkPathType(priority: Int) : Prioritized(priority) {

    abstract fun acceptsKey(page: OrchidPage, key: String): Boolean

    abstract fun format(page: OrchidPage, key: String): String?

}
