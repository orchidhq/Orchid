package com.eden.orchid.wiki;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.render.ContentFilter;
import com.eden.orchid.api.theme.menus.OrchidMenuItemType;
import com.eden.orchid.wiki.menu.WikiPagesMenuItemType;

public class WikiModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, WikiGenerator.class);
        addToSet(ContentFilter.class, WikiGlossaryTermFilter.class);

        addToMap(OrchidMenuItemType.class, "wiki", WikiPagesMenuItemType.class);
    }
}
