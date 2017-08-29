package com.eden.orchid.pages.menu;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PageMenuType implements OrchidMenuItemFactory {

    protected OrchidContext context;

    @Inject
    public PageMenuType(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String getKey() {
        return "page";
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        List<OrchidMenuItem> menuItems = new ArrayList<>();

        List<OrchidPage> pages = context.getInternalIndex().getGeneratorPages("pages");

        if(menuItemJson.has("page")) {
            for (OrchidPage page : pages) {
                if(page.getTitle().equals(menuItemJson.getString("page"))) {
                    menuItems.add(new OrchidMenuItem(context, page));
                }
            }
        }

        return menuItems;
    }
}
