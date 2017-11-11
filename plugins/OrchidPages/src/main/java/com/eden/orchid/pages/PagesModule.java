package com.eden.orchid.pages;

import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.pages.menu.PageMenuType;
import com.eden.orchid.pages.menu.PagesMenuType;

public class PagesModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, PagesGenerator.class);

        addToSet(OrchidMenuItem.class,
                PagesMenuType.class,
                PageMenuType.class);
    }
}
