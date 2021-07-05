package com.eden.orchid.wiki.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Archetypes
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.render.RenderService
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.utilities.extractOptionsFromResource
import com.eden.orchid.wiki.WikiGenerator
import com.eden.orchid.wiki.WikiSectionArchetype

@Archetypes(
    Archetype(value = WikiSectionArchetype::class, key = WikiGenerator.GENERATOR_KEY),
    Archetype(value = ConfigArchetype::class, key = "${WikiGenerator.GENERATOR_KEY}.wikiPages")
)
@Description(value = "An entry in your wiki.", name = "Wiki")
class WikiPage(
    resource: OrchidResource,
    title: String,
    var section: String?,
    val order: Int
) :
    OrchidPage(resource, RenderService.RenderMode.TEMPLATE, "wiki", title) {

    lateinit var sectionSummary: WikiSummaryPage

    init {
        data = extractOptionsFromResource(context, resource)
        postInitialize(title)
    }

    override fun initialize(title: String?) {
    }
}
