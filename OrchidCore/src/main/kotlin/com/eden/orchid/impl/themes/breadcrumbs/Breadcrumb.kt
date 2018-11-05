package com.eden.orchid.impl.themes.breadcrumbs

import com.eden.orchid.api.theme.pages.OrchidPage

class Breadcrumb(
        val page: OrchidPage?,
        var title: String = page?.title ?: ""
) {

    val link: String?
        get() = if (page != null)
            page.link
        else
            null

    // have both of these methods for parity with OrchidMenuItemImpl
    fun isActivePage(page: OrchidPage): Boolean {
        return this.page === page
    }

    fun isActive(page: OrchidPage): Boolean {
        return isActivePage(page)
    }

    @JvmOverloads
    fun activeClass(page: OrchidPage, className: String = "active"): String {
        return if (isActive(page)) className else ""
    }

}
