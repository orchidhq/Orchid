package com.eden.orchid.wiki

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.wiki.menu.WikiPagesMenuItemType
import com.eden.orchid.wiki.menu.WikiSectionsMenuItemType

class WikiModule : OrchidModule() {

    override fun configure() {
        addToSet(OrchidGenerator::class.java,
                WikiGenerator::class.java)

        addToSet(OrchidMenuItem::class.java,
                WikiPagesMenuItemType::class.java,
                WikiSectionsMenuItemType::class.java)
    }
}

