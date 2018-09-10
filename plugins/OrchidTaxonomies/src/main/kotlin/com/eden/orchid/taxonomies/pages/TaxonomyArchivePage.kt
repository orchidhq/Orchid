package com.eden.orchid.taxonomies.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.taxonomies.TaxonomiesGenerator
import com.eden.orchid.taxonomies.models.TaxonomiesModel
import com.eden.orchid.taxonomies.models.Taxonomy

@Archetype(value = ConfigArchetype::class, key = "${TaxonomiesGenerator.GENERATOR_KEY}.taxonomyArchivePages")
open class TaxonomyArchivePage(
        resource: OrchidResource,
        val model: TaxonomiesModel,
        val taxonomy: Taxonomy,
        val index: Int
) : OrchidPage(resource, "taxonomyArchive", taxonomy.title) {

    override fun getTemplates(): List<String> {
        val templates = mutableListOf(
                "${this.key}-${taxonomy.key}"
        )
        templates.addAll(super.getTemplates())

        return templates
    }

}

