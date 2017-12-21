package com.eden.orchid.wiki.pages

import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage

class WikiPage(
        resource: OrchidResource,
        title: String,
        var order: Int)
    : OrchidPage(resource, "wiki", title) {

    lateinit var sectionSummary: WikiSummaryPage

}
