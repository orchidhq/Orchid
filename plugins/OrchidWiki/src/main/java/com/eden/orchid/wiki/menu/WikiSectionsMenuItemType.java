package com.eden.orchid.wiki.menu;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.wiki.WikiGenerator;
import com.eden.orchid.wiki.WikiPage;
import com.eden.orchid.wiki.WikiSummaryPage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WikiSectionsMenuItemType extends OrchidMenuItem {

    @Inject
    public WikiSectionsMenuItemType(OrchidContext context) {
        super(context, "wikiSections", 100);
    }

    @Override
    public List<OrchidMenuItemImpl> getMenuItems() {
        List<OrchidMenuItemImpl> menuItems = new ArrayList<>();

        for (Map.Entry<String, EdenPair<WikiSummaryPage, List<WikiPage>>> section : WikiGenerator.sections.entrySet()) {
            menuItems.add(new OrchidMenuItemImpl(context, section.getValue().first));
        }

        return menuItems;
    }

}
