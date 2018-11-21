package com.eden.orchid.kss.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.kss.model.KssModel
import javax.inject.Inject

@Description("Link to all the sections of your styleguide.", name = "Styleguide Sections")
class StyleguideSectionsMenuItemType
@Inject
constructor(
        context: OrchidContext,
        private val model: KssModel
) : OrchidMenuFactory(context, "styleguideSections", 100) {

    @Option @StringDefault("Styleguide")
    @Description("The title of the root menu item.")
    lateinit var title: String

    override fun getMenuItems(): List<MenuItem> {
        val menuItems = ArrayList<MenuItem>()

        model.sections.keys.forEach {
            val pages = model.getSectionRoot(it)
            if(pages.isNotEmpty()) {
                menuItems.add(
                        MenuItem.Builder(context)
                                .title(title)
                                .pages(pages)
                                .build()
                )
            }
        }

        return menuItems
    }
}

