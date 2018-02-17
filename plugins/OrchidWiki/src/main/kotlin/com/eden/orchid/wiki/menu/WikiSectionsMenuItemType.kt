package com.eden.orchid.wiki.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.wiki.model.WikiModel
import javax.inject.Inject

class WikiSectionsMenuItemType @Inject
constructor(context: OrchidContext, private val model: WikiModel) : OrchidMenuItem(context, "wikiSections", 100) {

    @Option @BooleanDefault(true)
    @Description("Whether to keep the wiki pages as children of a single menu item, or expand them all to the root.")
    var topLevel: Boolean = false

    @Option @StringDefault("Wiki Sections")
    @Description("If `topLevel` is false, the title for the root menu item.")
    lateinit var title: String

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val sectionMenuItems = model.sections.values
                .map { OrchidMenuItemImpl(context, it.summaryPage) }
                .toList()

        return if(topLevel) {
            sectionMenuItems
        }
        else {
            listOf(OrchidMenuItemImpl(context, sectionMenuItems, title))
        }
    }

}

