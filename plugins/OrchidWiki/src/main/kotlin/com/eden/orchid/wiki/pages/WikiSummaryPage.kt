package com.eden.orchid.wiki.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Archetypes
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.wiki.WikiSectionArchetype

@Archetypes(
        Archetype(value = ConfigArchetype::class, key = "wikiPages"),
        Archetype(value = WikiSectionArchetype::class, key = "wikiPages")
)
class WikiSummaryPage(val section: String?, resource: OrchidResource, title: String)
    : OrchidPage(resource, "wikiSummary", title)
