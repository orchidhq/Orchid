package com.eden.orchid.pages;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.pages.menu.AllPagesMenuType;
import com.eden.orchid.pages.menu.PageMenuType;

public class PagesModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, PagesGenerator.class);

        addToSet(OrchidMenuItem.class,
                AllPagesMenuType.class,
                PageMenuType.class);
    }
}
