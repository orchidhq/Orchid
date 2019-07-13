package com.eden.orchid.groovydoc.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.groovydoc.models.GroovydocModel
import java.util.ArrayList

@Description("All groovydoc package pages.", name = "groovydoc Packages")
class AllPackagesMenuItemType : OrchidMenuFactory("groovydocPackages") {

    @Option
    @StringDefault("All Packages")
    lateinit var title: String

    override fun getMenuItems(context: OrchidContext): List<MenuItem> {
        val model = context.resolve(GroovydocModel::class.java)

        val items = ArrayList<MenuItem>()
        val pages = ArrayList<OrchidPage>(model.allPackages)
        pages.sortBy { it.title }
        items.add(
            MenuItem.Builder(context)
                .title(title)
                .pages(pages)
                .build()
        )
        return items
    }

}
