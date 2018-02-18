package com.eden.orchid.kss.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.kss.model.KssModel
import com.eden.orchid.kss.pages.KssPage
import javax.inject.Inject

class StyleguidePagesMenuItemType @Inject
constructor(context: OrchidContext, private val model: KssModel) : OrchidMenuItem(context, "styleguide", 100) {

    @Option
    @Description("The Styleguide section to get pages for.")
    lateinit var section: String

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val menuItems = ArrayList<OrchidMenuItemImpl>()

        val pages = model.getSectionRoot(section)

        if(pages.isNotEmpty()) {
            menuItems.add(getChildMenuItem(null, pages))
        }

        return menuItems
    }

    private fun getChildMenuItem(parent: KssPage?, childPages: List<KssPage>): OrchidMenuItemImpl {

        val childMenuItems = ArrayList<OrchidMenuItemImpl>()
        val title: String?

        if(parent != null) {
            childMenuItems.add(OrchidMenuItemImpl(context, parent))
            title = parent.title
        }
        else {
            title = "Styleguide Pages"
        }

        childPages.forEach {
            childMenuItems.add(getChildMenuItem(it, it.children))
        }

        return OrchidMenuItemImpl(context, childMenuItems, title)
    }
}

