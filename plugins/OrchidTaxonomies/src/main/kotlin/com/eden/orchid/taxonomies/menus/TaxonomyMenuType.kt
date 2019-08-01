package com.eden.orchid.taxonomies.menus

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.taxonomies.models.TaxonomiesModel

@Description(
    "Link to a specific Taxonomy landing page, optionally with links to its Term landing pages underneath it.",
    name = "Taxonomy"
)
class TaxonomyMenuType : OrchidMenuFactory("taxonomy") {

    @Option
    @Description("The Taxonomy to include terms from.")
    lateinit var taxonomyType: String

    @Option
    @Description(
        "Whether to have the menu link out to the Taxonomy landing page, or include child menu items with " +
                "links out to the Term landing pages."
    )
    var includeTerms = false

    @Option
    @Description(
        "If `includeTerms` is true, whether to keep the terms as children of a single menu item, or expand " +
                "them all to the root."
    )
    var termsAtRoot = false

    override fun getMenuItems(
        context: OrchidContext,
        page: OrchidPage
    ): List<MenuItem> {
        val model = context.resolve(TaxonomiesModel::class.java)
        val taxonomy = model.taxonomies[taxonomyType]

        val items = ArrayList<MenuItem>()
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
                } else {
                    items.add(
                        MenuItem.Builder(context)
                            .title(taxonomy.title)
                            .pages(taxonomy.allTerms.map { it.archivePages.first() }.toList())
                            .build()
                    )
                }
            } else {
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

