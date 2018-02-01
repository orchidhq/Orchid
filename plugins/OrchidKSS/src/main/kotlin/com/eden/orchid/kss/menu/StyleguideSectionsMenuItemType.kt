package com.eden.orchid.kss.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.kss.model.KssModel
import javax.inject.Inject

class StyleguideSectionsMenuItemType @Inject
constructor(context: OrchidContext, private val model: KssModel) : OrchidMenuItem(context, "styleguideSections", 100) {

    @Option @StringDefault("Styleguide")
    lateinit var title: String

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val menuItems = ArrayList<OrchidMenuItemImpl>()

        model.sections.keys.forEach {
            val pages = model.getSectionRoot(it)
            if(pages.isNotEmpty()) {
                menuItems.add(OrchidMenuItemImpl(context, title, pages))
            }
        }

        return menuItems
    }
}

