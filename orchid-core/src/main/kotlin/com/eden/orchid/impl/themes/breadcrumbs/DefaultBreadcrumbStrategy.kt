package com.eden.orchid.impl.themes.breadcrumbs

import com.eden.orchid.api.theme.pages.OrchidPage
import java.util.ArrayList
import java.util.LinkedHashMap

class DefaultBreadcrumbStrategy : BreadcrumbStrategy {

    override fun getBreadcrumbs(page: OrchidPage): List<Breadcrumb> {
        val breadcrumbPagesMap = LinkedHashMap<OrchidPage, OrchidPage>()

        var parentPage: OrchidPage? = page
        while (parentPage != null) {
            if (breadcrumbPagesMap.containsKey(parentPage)) break // break out of a cycle

            breadcrumbPagesMap[parentPage] = parentPage
            parentPage = parentPage.parent
        }

        return ArrayList(breadcrumbPagesMap.values)
            .reversed()
            .map { Breadcrumb(it) }
    }
}
