package com.eden.orchid.pages;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.theme.menus.OrchidMenuItemType;
import com.eden.orchid.pages.menu.AllPagesMenuType;
import com.eden.orchid.pages.menu.PageMenuType;

public class PagesModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, PagesGenerator.class);

        addToMap(OrchidMenuItemType.class, "pages", AllPagesMenuType.class);
        addToMap(OrchidMenuItemType.class, "page", PageMenuType.class);
    }
}
