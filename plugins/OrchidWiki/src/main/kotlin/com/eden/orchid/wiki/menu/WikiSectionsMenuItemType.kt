package com.eden.orchid.wiki.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.wiki.model.WikiModel
import javax.inject.Inject

@Description("Links to each section in your wiki.", name = "Wiki Sections")
class WikiSectionsMenuItemType
@Inject
constructor(
        context: OrchidContext,
        private val model: WikiModel
) : OrchidMenuFactory(context, "wikiSections", 100) {

    override fun getMenuItems(): List<MenuItem> {
        return  model.sections.values
                .map {
                    MenuItem.Builder(context)
                            .title(it.sectionTitle)
                            .page(it.summaryPage)
                            .build()
                }
                .toList()
    }

}

