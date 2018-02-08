package com.eden.orchid.javadoc.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.javadoc.models.JavadocModel
import java.util.ArrayList
import javax.inject.Inject

class AllPackagesMenuItemType @Inject
constructor(context: OrchidContext, private val model: JavadocModel) : OrchidMenuItem(context, "javadocPackages", 100) {

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val items = ArrayList<OrchidMenuItemImpl>()
        val pages = ArrayList<OrchidPage>(model.allPackages)
        pages.sortBy { it.title }
        items.add(OrchidMenuItemImpl(context, "All Packages", pages))
        return items
    }

}
