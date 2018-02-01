package com.eden.orchid.taxonomies.menus

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.taxonomies.models.TaxonomiesModel
import com.eden.orchid.taxonomies.models.Taxonomy
import javax.inject.Inject

class TaxonomyMenuType @Inject
constructor(context: OrchidContext, var model: TaxonomiesModel) : OrchidMenuItem(context, "taxonomy", 100) {

    @Option
    lateinit var taxonomyType: String

    @Option
    var includeTerms = false

    @Option
    var termsAtRoot = false

    val taxonomy: Taxonomy?
        get() {
            return model.taxonomies[taxonomyType]
        }

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val items = ArrayList<OrchidMenuItemImpl>()
        val taxonomy = this.taxonomy
        if(taxonomy != null) {
            if(includeTerms) {
                if(termsAtRoot) {
                    taxonomy.allTerms.forEach {
                        items.add(OrchidMenuItemImpl(
                                context,
                                it.title,
                                it.archivePages.first()
                        ))
                    }
                }
                else {
                    items.add(OrchidMenuItemImpl(
                            context,
                            taxonomy.title,
                            taxonomy.allTerms.map { it.archivePages.first() }.toList()
                    ))
                }
            }
            else {
                items.add(OrchidMenuItemImpl(
                        context,
                        taxonomy.title,
                        taxonomy.archivePages.first()
                ))
            }
        }

        return items
    }

}

