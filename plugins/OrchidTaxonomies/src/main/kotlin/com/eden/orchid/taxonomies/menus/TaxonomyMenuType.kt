package com.eden.orchid.taxonomies.menus

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.taxonomies.models.TaxonomiesModel
import com.eden.orchid.taxonomies.models.Taxonomy
import javax.inject.Inject

@Description("Link to a specific Taxonomy landing page, optionally with links to its Term landing pages underneath it.",
        name = "Taxonomy"
)
class TaxonomyMenuType
@Inject
constructor(
        context: OrchidContext,
        var model: TaxonomiesModel
) : OrchidMenuFactory(context, "taxonomy", 100) {

    @Option
    @Description("The Taxonomy to include terms from.")
    lateinit var taxonomyType: String

    @Option
    @Description("Whether to have the menu link out to the Taxonomy landing page, or include child menu items with " +
            "links out to the Term landing pages."
    )
    var includeTerms = false

    @Option
    @Description("If `includeTerms` is true, whether to keep the terms as children of a single menu item, or expand " +
            "them all to the root."
    )
    var termsAtRoot = false

    val taxonomy: Taxonomy?
        get() {
            return model.taxonomies[taxonomyType]
        }

    override fun getMenuItems(): List<MenuItem> {
        val items = ArrayList<MenuItem>()
        val taxonomy = this.taxonomy
        if (taxonomy != null) {
            if (includeTerms) {
                if (termsAtRoot) {
                    taxonomy.allTerms.forEach {
                        items.add(
                                MenuItem.Builder(context)
                                        .title(it.title)
                                        .page(it.archivePages.first())
                                        .build()
                        )
                    }
                }
                else {
                    items.add(
                            MenuItem.Builder(context)
                                    .title(taxonomy.title)
                                    .pages(taxonomy.allTerms.map { it.archivePages.first() }.toList())
                                    .build()
                    )
                }
            }
            else {
                items.add(
                        MenuItem.Builder(context)
                                .title(taxonomy.title)
                                .page(taxonomy.archivePages.first())
                                .build()
                )
            }
        }

        return items
    }

}

