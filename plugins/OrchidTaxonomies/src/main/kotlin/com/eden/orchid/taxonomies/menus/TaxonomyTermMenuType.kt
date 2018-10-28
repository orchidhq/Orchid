package com.eden.orchid.taxonomies.menus

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.taxonomies.models.TaxonomiesModel
import com.eden.orchid.taxonomies.models.Taxonomy
import com.eden.orchid.taxonomies.models.Term
import javax.inject.Inject

@Description("Link to a specific Taxonomy Term landing page, optionally with links to its associated pages.",
        name = "Taxonomy Term"
)
class TaxonomyTermMenuType
@Inject
constructor(
        context: OrchidContext,
        var model: TaxonomiesModel
) : OrchidMenuItem(context, "taxonomyTerm", 100) {

    @Option
    @Description("The Taxonomy to include terms from.")
    lateinit var taxonomyType: String

    @Option
    @Description("The Term within the Taxonomy to include pages from.")
    lateinit var termType: String

    @Option
    @Description("Whether to have the menu link out to the Term landing page, or include child menu items with " +
            "links out to the Term's associated pages."
    )
    var includePages = false

    @Option
    @Description("If `includePages` is true, whether to keep the associated pages as children of a single menu item, " +
            "or expand them all to the root."
    )
    var pagesAtRoot = false

    @Option
    @IntDefault(4)
    @Description("The maximum number of associated pages to include in this menu item.")
    var limit: Int = 4

    val taxonomy: Taxonomy?
        get() {
            return model.taxonomies[taxonomyType]
        }

    val term: Term?
        get() {
            return taxonomy?.terms?.get(termType)
        }

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val items = ArrayList<OrchidMenuItemImpl>()
        val term = this.term
        if (term != null) {
            if (includePages) {
                val pageList = if (term.allPages.size > limit) term.allPages.subList(0, limit) else term.allPages

                if (pagesAtRoot) {
                    pageList.forEach {
                        items.add(OrchidMenuItemImpl.Builder(context).page(it).build())
                    }
                }
                else {
                    items.add(OrchidMenuItemImpl.Builder(context).title(term.title).pages(pageList).build())
                }
            }
            else {
                items.add(OrchidMenuItemImpl.Builder(context)
                        .title(term.title)
                        .page(term.archivePages.first())
                        .build()
                )
            }
        }

        return items
    }

}

