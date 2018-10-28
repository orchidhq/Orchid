package com.eden.orchid.taxonomies.menus

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.taxonomies.models.TaxonomiesModel
import javax.inject.Inject

@Description("Links to all the Taxonomy landing pages.", name = "All Taxonomies")
class TaxonomiesMenuType
@Inject
constructor(
        context: OrchidContext,
        var model: TaxonomiesModel
) : OrchidMenuItem(context, "taxonomies", 100) {

    @Option
    @StringDefault("Taxonomies")
    @Description("The menu item title.")
    lateinit var title: String

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val items = ArrayList<OrchidMenuItemImpl>()

        model.taxonomies.values.forEach {
            items.add(OrchidMenuItemImpl.Builder(context)
                    .title(it.title)
                    .page(it.archivePages.first())
                    .build()
            )
        }

        return items
    }

}

