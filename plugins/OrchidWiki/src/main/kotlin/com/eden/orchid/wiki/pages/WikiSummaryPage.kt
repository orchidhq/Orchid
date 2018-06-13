package com.eden.orchid.wiki.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Archetypes
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.wiki.WikiGenerator
import com.eden.orchid.wiki.WikiSectionArchetype

@Archetypes(
        Archetype(value = ConfigArchetype::class, key = "${WikiGenerator.GENERATOR_KEY}.wikiPages"),
        Archetype(value = WikiSectionArchetype::class, key = "${WikiGenerator.GENERATOR_KEY}.wikiPages")
)
class WikiSummaryPage(val section: String?, resource: OrchidResource, title: String)
    : OrchidPage(resource, "wikiSummary", title) {

    var sectionsPage: WikiSectionsPage? = null
}
