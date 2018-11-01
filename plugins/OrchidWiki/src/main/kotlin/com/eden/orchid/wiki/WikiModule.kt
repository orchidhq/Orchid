package com.eden.orchid.wiki

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.utilities.addToSet
import com.eden.orchid.wiki.menu.WikiPagesMenuItemType
import com.eden.orchid.wiki.menu.WikiSectionsMenuItemType

class WikiModule : OrchidModule() {

    override fun configure() {
        withResources(50)

        addToSet<OrchidGenerator, WikiGenerator>()
        addToSet<OrchidMenuFactory>(
                WikiPagesMenuItemType::class,
                WikiSectionsMenuItemType::class)
    }
}

