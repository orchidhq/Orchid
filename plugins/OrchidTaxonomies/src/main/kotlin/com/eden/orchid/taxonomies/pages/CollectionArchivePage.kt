package com.eden.orchid.taxonomies.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.taxonomies.TaxonomiesGenerator
import com.eden.orchid.taxonomies.models.CollectionArchive
import com.eden.orchid.taxonomies.models.TaxonomiesModel

@Archetype(value = ConfigArchetype::class, key = "${TaxonomiesGenerator.GENERATOR_KEY}.termArchivePages")
@Description(value = "A paginated page for all the Pages in a Taxonomy Term.", name = "Taxonomy Term")
open class CollectionArchivePage(
    resource: OrchidResource,
    val model: TaxonomiesModel,
    val pageList: List<OrchidPage>,
    val collectionArchive: CollectionArchive,
    val index: Int
) : OrchidPage(resource, "collectionArchive", collectionArchive.title) {

    override val itemIds: List<String> = listOf(collectionArchive.key)

    override fun getTemplates(): List<String> {
        return mutableListOf<String>().also {
            it.add("${this.key}-${collectionArchive.key}")
            if(collectionArchive.collectionType.isNotBlank()) {
                if(collectionArchive.collectionId.isNotBlank()) {
                    it.add("${this.key}-${collectionArchive.collectionType}-${collectionArchive.collectionId}")
                }

                it.add("${this.key}-${collectionArchive.collectionType}")
            }
        }
    }

}

