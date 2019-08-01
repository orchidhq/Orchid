package com.eden.orchid.sourcedoc.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenu
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.sourcedoc.page.SourceDocPage
import java.util.ArrayList

@Description(
    "Links to the different sections within a Javadoc Class page, optionally with their items nested " +
            "underneath them.",
    name = "Javadoc Class Sections"
)
class SourceDocPageLinksMenuItemType : OrchidMenuFactory("sourcedocPageLinks") {

    @Option
    @BooleanDefault(false)
    @Description(
        "Whether to include the items for each category. For example, including a menu item for each " +
                "individual constructor as children of 'Constructors' or just a link to the Constructors section."
    )
    var includeItems: Boolean = false

    override fun canBeUsedOnPage(
        containingPage: OrchidPage,
        menu: OrchidMenu,
        possibleMenuItems: List<Map<String, Any>>,
        currentMenuItems: List<OrchidMenuFactory>
    ): Boolean {
        return containingPage is SourceDocPage<*>
    }

    override fun getMenuItems(
        context: OrchidContext,
        page: OrchidPage
    ): List<MenuItem> {
//        val model = context.resolve(SourceDocModel::class.java, page.generator.key)
        val containingPage = page as SourceDocPage<*>

        val menuItems = ArrayList<MenuItem>()

        val pageSections = containingPage.getSectionsData(containingPage.element)

        for(section in pageSections) {
            val menuItem = MenuItem.Builder(context)
                .title(section.name)

            menuItems.add(menuItem.build())
        }

        return menuItems
    }
}
