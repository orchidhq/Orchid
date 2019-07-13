package com.eden.orchid.wiki.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.wiki.model.WikiModel

@Description("Links to each section in your wiki.", name = "Wiki Sections")
class WikiSectionsMenuItemType : OrchidMenuFactory("wikiSections") {

    override fun getMenuItems(context: OrchidContext): List<MenuItem> {
        val model: WikiModel = context.resolve(WikiModel::class.java)
        return model.sections.values
            .map {
                MenuItem.Builder(context)
                    .title(it.sectionTitle)
                    .page(it.summaryPage)
                    .build()
            }
            .toList()
    }

}

