package com.eden.orchid.wiki;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import com.eden.orchid.wiki.menu.WikiPagesMenuItemType;
import com.eden.orchid.wiki.menu.WikiSectionsMenuItemType;

public class WikiModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, WikiGenerator.class);

        addToMap(OrchidMenuItemFactory.class, "wiki", WikiPagesMenuItemType.class);
        addToMap(OrchidMenuItemFactory.class, "wikiSections", WikiSectionsMenuItemType.class);
    }
}
