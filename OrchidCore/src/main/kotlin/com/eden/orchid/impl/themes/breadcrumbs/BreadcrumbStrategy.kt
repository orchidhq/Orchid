package com.eden.orchid.impl.themes.breadcrumbs

import com.eden.orchid.api.theme.pages.OrchidPage
import com.google.inject.ImplementedBy

@ImplementedBy(DefaultBreadcrumbStrategy::class)
interface BreadcrumbStrategy {

    fun getBreadcrumbs(page: OrchidPage): List<Breadcrumb>
}
