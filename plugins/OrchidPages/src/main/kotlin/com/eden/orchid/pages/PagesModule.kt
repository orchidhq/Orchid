package com.eden.orchid.pages

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.pages.menu.PageMenuType
import com.eden.orchid.pages.menu.PagesMenuType

class PagesModule : OrchidModule() {

    override fun configure() {
        addToSet(OrchidGenerator::class.java,
                PagesGenerator::class.java)

        addToSet(OrchidMenuItem::class.java,
                PagesMenuType::class.java,
                PageMenuType::class.java)
    }
}

