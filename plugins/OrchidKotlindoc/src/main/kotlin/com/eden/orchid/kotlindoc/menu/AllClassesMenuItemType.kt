package com.eden.orchid.kotlindoc.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.kotlindoc.model.KotlindocModel
import javax.inject.Inject

@Description("All Kotlindoc class pages.", name = "Kotlindoc Classes")
class AllClassesMenuItemType
@Inject
constructor(
        context: OrchidContext,
        private val model: KotlindocModel
) : OrchidMenuItem(context, "kotlindocClasses", 100) {

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val items = ArrayList<OrchidMenuItemImpl>()
        val pages = ArrayList<OrchidPage>(model.allClasses)
        pages.sortBy { it.title }
        items.add(OrchidMenuItemImpl(context, "All Classes", pages))
        return items
    }

}
