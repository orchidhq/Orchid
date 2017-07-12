package com.eden.orchid.languages.menu;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.OrchidMenuItemType;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PageMenuType implements OrchidMenuItemType {

    protected OrchidContext context;

    @Inject
    public PageMenuType(OrchidContext context) {
        this.context = context;
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
