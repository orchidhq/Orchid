package com.eden.orchid.wiki.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.wiki.model.WikiModel
import javax.inject.Inject

@Description("Links to each section in your wiki.", name = "Wiki Sections")
class WikiSectionsMenuItemType
@Inject
constructor(
        context: OrchidContext,
        private val model: WikiModel
) : OrchidMenuItem(context, "wikiSections", 100) {

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        return  model.sections.values
                .map { OrchidMenuItemImpl(context, it.sectionTitle, it.summaryPage) }
                .toList()
    }

}

