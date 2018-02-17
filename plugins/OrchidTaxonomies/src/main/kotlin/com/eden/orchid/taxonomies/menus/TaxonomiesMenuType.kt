package com.eden.orchid.taxonomies.menus

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.taxonomies.models.TaxonomiesModel
import javax.inject.Inject

class TaxonomiesMenuType @Inject
constructor(context: OrchidContext, var model: TaxonomiesModel) : OrchidMenuItem(context, "taxonomies", 100) {

    @Option @StringDefault("Taxonomies")
    @Description("The menu item title.")
    lateinit var title: String

    @Option
    @Description("Whether to keep the taxonomies as children of a single menu item, or expand them all to the root.")
    var taxonomiesAtRoot = false

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val items = ArrayList<OrchidMenuItemImpl>()
        if(taxonomiesAtRoot) {
            model.taxonomies.values.forEach {
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
                    title,
                    model.taxonomies.values.map { it.archivePages.first() }.toList()
            ))
        }

        return items
    }

}

