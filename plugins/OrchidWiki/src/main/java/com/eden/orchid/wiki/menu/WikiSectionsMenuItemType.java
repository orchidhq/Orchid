package com.eden.orchid.wiki.menu;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import com.eden.orchid.wiki.WikiGenerator;
import com.eden.orchid.wiki.WikiPage;
import com.eden.orchid.wiki.WikiSummaryPage;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WikiSectionsMenuItemType implements OrchidMenuItemFactory {

    private OrchidContext context;

    @Inject
    public WikiSectionsMenuItemType(OrchidContext context) {
        this.context = context;
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        List<OrchidMenuItem> menuItems = new ArrayList<>();

        for (Map.Entry<String, EdenPair<WikiSummaryPage, List<WikiPage>>> section : WikiGenerator.sections.entrySet()) {
            menuItems.add(new OrchidMenuItem(context, section.getValue().first));
        }

        return menuItems;
    }
}
