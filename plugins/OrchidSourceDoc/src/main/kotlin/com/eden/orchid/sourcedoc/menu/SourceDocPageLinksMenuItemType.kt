package com.eden.orchid.sourcedoc.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
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

    enum class ItemTitleType {
        NAME, SIGNATURE
    }

    @Option
    @BooleanDefault(false)
    @Description(
        "Whether to include the items for each section. For example, including a menu item for each " +
                "individual constructor as children of `Constructors` or just a link to the `Constructors` section."
    )
    var includeItems: Boolean = false

    @Option
    @StringDefault("NAME")
    lateinit var itemTitleType: ItemTitleType

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
        try {
            val containingPage = page as SourceDocPage<*>

            val menuItems = ArrayList<MenuItem>()

            val pageSections = containingPage.getSectionsData(containingPage.element)

            for (section in pageSections) {
                val menuItem = MenuItem.Builder(context)
                    .title(section.name)
                    .anchor(containingPage.sectionId(section))

                if (includeItems) {
                    section
                        .elements
                        .map { sectionElement ->
                            val itemMenuBuilder = MenuItem.Builder(context)

                            // set title
                            val itemTitle = when (itemTitleType) {
                                ItemTitleType.NAME -> sectionElement.name
                                ItemTitleType.SIGNATURE -> sectionElement.signature.joinToString("") { it.text }
                            }
                            itemMenuBuilder.title(itemTitle)
                            itemMenuBuilder.anchor(containingPage.elementId(sectionElement))
                        }
                        .forEach { menuItem.child(it) }
                }

                menuItems.add(menuItem.build())
            }

            return menuItems
        }
        catch (t: Throwable) {
            t.printStackTrace()
            return emptyList()
        }
    }
}
