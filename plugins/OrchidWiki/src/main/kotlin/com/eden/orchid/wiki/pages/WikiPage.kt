package com.eden.orchid.wiki.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage

@Archetype(value = ConfigArchetype::class, key = "wikiPages")
class WikiPage(
        resource: OrchidResource,
        title: String,
        var order: Int)
    : OrchidPage(resource, "wiki", title) {

    lateinit var sectionSummary: WikiSummaryPage

}
