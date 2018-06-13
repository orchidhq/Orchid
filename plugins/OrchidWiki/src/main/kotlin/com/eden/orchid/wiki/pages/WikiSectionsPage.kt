package com.eden.orchid.wiki.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Archetypes
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.wiki.WikiGenerator
import com.eden.orchid.wiki.model.WikiModel

@Archetypes(
        Archetype(value = ConfigArchetype::class, key = "${WikiGenerator.GENERATOR_KEY}.wikiPages")
)
class WikiSectionsPage(val model: WikiModel, resource: OrchidResource, title: String)
    : OrchidPage(resource, "wikiSections", title) {

    val sectionPages: List<WikiSummaryPage>
        get() {
            return model.sections.values.map { it.summaryPage }
        }

}
