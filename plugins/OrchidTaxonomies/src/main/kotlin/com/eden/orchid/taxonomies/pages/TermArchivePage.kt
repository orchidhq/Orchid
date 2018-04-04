package com.eden.orchid.taxonomies.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.taxonomies.TaxonomiesGenerator
import com.eden.orchid.taxonomies.models.TaxonomiesModel
import com.eden.orchid.taxonomies.models.Taxonomy
import com.eden.orchid.taxonomies.models.Term

@Archetype(value = ConfigArchetype::class, key = "${TaxonomiesGenerator.GENERATOR_KEY}.termArchivePages")
open class TermArchivePage(
        resource: OrchidResource,
        val model: TaxonomiesModel,
        val pageList: List<OrchidPage>,
        val taxonomy: Taxonomy,
        val term: Term,
        val index: Int
) : OrchidPage(resource, "termArchive") {

    override fun getTemplates(): List<String> {
        val templates = mutableListOf(
                "${this.key}-${taxonomy.key}-${term.key}",
                "${this.key}-${taxonomy.key}"
        )
        templates.addAll(super.getTemplates())

        return templates
    }

}

