package com.eden.orchid.wiki.menu;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.wiki.model.WikiModel;
import com.eden.orchid.wiki.model.WikiSection;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WikiSectionsMenuItemType extends OrchidMenuItem {

    private final WikiModel model;

    @Inject
    public WikiSectionsMenuItemType(OrchidContext context, WikiModel model) {
        super(context, "wikiSections", 100);
        this.model = model;
    }

    @Override
    public List<OrchidMenuItemImpl> getMenuItems() {
        List<OrchidMenuItemImpl> menuItems = new ArrayList<>();

        for (Map.Entry<String, WikiSection> section : model.getSections().entrySet()) {
            menuItems.add(new OrchidMenuItemImpl(context, section.getValue().getSummaryPage()));
        }

        return menuItems;
    }

}
