package com.eden.orchid.taxonomies.menus

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.taxonomies.models.TaxonomiesModel
import com.eden.orchid.taxonomies.models.Taxonomy
import com.eden.orchid.taxonomies.models.Term
import java.util.*
import javax.inject.Inject

class TaxonomyTermMenuType @Inject
constructor(context: OrchidContext, var model: TaxonomiesModel) : OrchidMenuItem(context, "taxonomyTerm", 100) {

    @Option
    lateinit var taxonomyType: String

    @Option
    lateinit var termType: String

    @Option
    var includePages = false

    @Option @IntDefault(4)
    var pageCount: Int = 4

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
        if(term != null) {
            if(includePages) {
                items.add(OrchidMenuItemImpl(
                        context,
                        term.title,
                        term.allPages.subList(0, pageCount)
                ))
            }
            else {
                items.add(OrchidMenuItemImpl(
                        context,
                        term.title,
                        term.archivePages.first()
                ))
            }
        }

        return items
    }

}

