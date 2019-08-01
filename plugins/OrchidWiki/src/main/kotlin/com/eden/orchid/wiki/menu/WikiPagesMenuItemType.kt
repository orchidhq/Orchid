package com.eden.orchid.wiki.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.indexing.OrchidIndex
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.wiki.model.WikiModel
import com.eden.orchid.wiki.model.WikiSection

@Description("Links to all pages in your wiki, optionally by section.", name = "Wiki Pages")
class WikiPagesMenuItemType : OrchidMenuFactory("wiki") {

    @Option
    @Description("The wiki section to include in this menu.")
    lateinit var section: String

    override fun getMenuItems(
        context: OrchidContext,
        page: OrchidPage
    ): List<MenuItem> {
        val model: WikiModel = context.resolve(WikiModel::class.java)
        val menuItems = ArrayList<MenuItem>()

        val sections = HashMap<String?, WikiSection>()

        val menuItemTitle: String

        // multiple sections to choose from
        if (model.sections.size > 1) {
            // we're specifying a single section to show
            if (section.isNotBlank()) {
                val wikiSection = model.getSection(section)
                if (wikiSection != null) {
                    sections.put(section, wikiSection)
                    menuItemTitle = wikiSection.title
                } else {
                    menuItemTitle = "Wiki"
                }
            }
            // did not specify a section, include them all
            else {
                sections.putAll(model.sections)
                menuItemTitle = "Wiki"
            }
        }
        // we only have the default section, so add it
        else {
            sections.putAll(model.sections)
            menuItemTitle = "Wiki"
        }

        if (submenuTitle.isBlank()) {
            submenuTitle = menuItemTitle
        }

        val wikiPagesIndex = OrchidIndex(null, "wiki")

        for (value in sections.values) {
            for (wikiPage in value.wikiPages) {
                wikiPagesIndex.addToIndex(wikiPage.reference.path, wikiPage)
            }
        }

        menuItems.add(
            MenuItem.Builder(context)
                .fromIndex(wikiPagesIndex)
                .build()
        )

        for (item in menuItems) {
            item.indexComparator = wikiMenuItemComparator
        }

        val innerItems = ArrayList<MenuItem>()
        for (item in menuItems) {
            if (item.isHasChildren) {
                innerItems.addAll(item.children)
            } else {
                innerItems.add(item)
            }
        }

        return if (model.sections.size > 1 && section.isNotBlank()) {
            innerItems.first().children
        } else {
            innerItems
        }
    }

}

