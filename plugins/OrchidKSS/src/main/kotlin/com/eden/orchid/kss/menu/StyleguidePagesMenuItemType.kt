package com.eden.orchid.kss.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.kss.model.KssModel
import com.eden.orchid.kss.pages.KssPage
import javax.inject.Inject

@Description("All pages in your styleguide, optionally by section.", name = "Styleguide")
class StyleguidePagesMenuItemType
@Inject
constructor(
        context: OrchidContext,
        private val model: KssModel
) : OrchidMenuFactory(context, "styleguide", 100) {

    @Option
    @Description("The Styleguide section to get pages for.")
    lateinit var section: String

    override fun getMenuItems(): List<MenuItem> {
        val menuItems = ArrayList<MenuItem>()

        val pages = model.getSectionRoot(section)

        if(pages.isNotEmpty()) {
            menuItems.add(getChildMenuItem(null, pages))
        }

        return menuItems
    }

    private fun getChildMenuItem(parent: KssPage?, childPages: List<KssPage>): MenuItem {

        val childMenuItems = ArrayList<MenuItem>()
        val title: String?

        if(parent != null) {
            childMenuItems.add(MenuItem.Builder(context).page(parent).build())
            title = parent.title
        }
        else {
            title = "Styleguide Pages"
        }

        childPages.forEach {
            childMenuItems.add(getChildMenuItem(it, it.children))
        }

        return MenuItem.Builder(context)
                .title(title)
                .children(childMenuItems)
                .build()
    }
}

