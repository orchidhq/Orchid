package com.eden.orchid.taxonomies.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.taxonomies.TaxonomiesGenerator
import com.eden.orchid.taxonomies.models.TaxonomiesModel
import com.eden.orchid.taxonomies.models.Taxonomy

@Archetype(value = ConfigArchetype::class, key = "${TaxonomiesGenerator.GENERATOR_KEY}.taxonomyArchivePages")
@Description(value = "A paginated page for all the Terms in a Taxonomy.", name = "Taxonomy")
open class TaxonomyArchivePage(
    resource: OrchidResource,
    val model: TaxonomiesModel,
    val taxonomy: Taxonomy,
    val index: Int
) : OrchidPage(resource, "taxonomyArchive", taxonomy.title) {

    override val itemIds: List<String> = listOf(taxonomy.key)

    override fun getTemplates(): List<String> {
        return listOf("${this.key}-${taxonomy.key}")
    }

}

