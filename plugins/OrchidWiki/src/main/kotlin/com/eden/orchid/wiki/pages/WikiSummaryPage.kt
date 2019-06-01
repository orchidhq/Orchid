package com.eden.orchid.wiki.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Archetypes
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.wiki.WikiGenerator
import com.eden.orchid.wiki.WikiSectionArchetype

@Archetypes(
    Archetype(value = WikiSectionArchetype::class, key = WikiGenerator.GENERATOR_KEY),
    Archetype(value = ConfigArchetype::class, key = "${WikiGenerator.GENERATOR_KEY}.wikiPages")
)
@Description(value = "The landing page for a wiki section.", name = "Wiki Summary")
class WikiSummaryPage(val section: String?, resource: OrchidResource, title: String)
    : OrchidPage(resource, "wikiSummary", title) {

    var sectionsPage: WikiSectionsPage? = null

    init {
        this.extractOptions(this.context, data)
        postInitialize(title)
    }

    override fun initialize(title: String?) {

    }
}
