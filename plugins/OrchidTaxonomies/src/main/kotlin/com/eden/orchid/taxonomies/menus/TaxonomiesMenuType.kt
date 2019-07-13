package com.eden.orchid.taxonomies.menus

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.taxonomies.models.TaxonomiesModel

@Description("Links to all the Taxonomy landing pages.", name = "All Taxonomies")
class TaxonomiesMenuType : OrchidMenuFactory("taxonomies") {

    @Option
    @StringDefault("Taxonomies")
    @Description("The menu item title.")
    lateinit var title: String

    override fun getMenuItems(context: OrchidContext): List<MenuItem> {
        val model = context.resolve(TaxonomiesModel::class.java)

        val items = ArrayList<MenuItem>()

        model.taxonomies.values.forEach {
            items.add(
                MenuItem.Builder(context)
                    .title(it.title)
                    .page(it.archivePages.first())
                    .build()
            )
        }

        return items
    }

}

