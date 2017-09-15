package com.eden.orchid.kss;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import com.eden.orchid.kss.menu.StyleguidePagesMenuItemType;
import com.eden.orchid.kss.menu.StyleguideSectionsMenuItemType;

public class KssModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, KssGenerator.class);

        addToSet(OrchidMenuItemFactory.class,
                StyleguidePagesMenuItemType.class,
                StyleguideSectionsMenuItemType.class);

        addToSet(DefaultResourceSource.class,
                KssResourceSource.class);
    }
}
