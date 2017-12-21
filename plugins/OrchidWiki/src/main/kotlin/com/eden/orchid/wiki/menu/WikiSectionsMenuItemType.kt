package com.eden.orchid.wiki.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.wiki.model.WikiModel
import javax.inject.Inject

class WikiSectionsMenuItemType @Inject
constructor(context: OrchidContext, private val model: WikiModel) : OrchidMenuItem(context, "wikiSections", 100) {

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        return model.sections.values
                .map { OrchidMenuItemImpl(context, it.summaryPage) }
                .toList()
    }

}
