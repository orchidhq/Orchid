package com.eden.orchid.wiki.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Archetypes
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.render.RenderService
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.wiki.WikiGenerator
import com.eden.orchid.wiki.model.WikiModel

@Archetypes(
    Archetype(value = ConfigArchetype::class, key = "${WikiGenerator.GENERATOR_KEY}.wikiPages")
)
@Description(value = "A landing page detailing all wiki sections.", name = "Wiki Sections")
class WikiSectionsPage(val model: WikiModel, resource: OrchidResource, title: String) :
    OrchidPage(resource, RenderService.RenderMode.TEMPLATE, "wikiSections", title) {

    val sectionPages: List<WikiSummaryPage>
        get() {
            return model.sections.values.map { it.summaryPage }
        }
}
