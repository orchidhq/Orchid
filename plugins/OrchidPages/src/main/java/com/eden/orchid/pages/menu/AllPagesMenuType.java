package com.eden.orchid.pages.menu;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.OrchidMenuItemType;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AllPagesMenuType implements OrchidMenuItemType {

    protected OrchidContext context;

    @Inject
    public AllPagesMenuType(OrchidContext context) {
        this.context = context;
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        List<OrchidMenuItem> menuItems = new ArrayList<>();

        List<OrchidPage> pages = context.getInternalIndex().getGeneratorPages("pages");

        if(menuItemJson.has("atRoot") && menuItemJson.getBoolean("atRoot")) {
            for(OrchidPage page : pages) {
                menuItems.add(new OrchidMenuItem(context, page));
            }
        }
        else {
            menuItems.add(new OrchidMenuItem(context, "Pages", pages));
        }

        return menuItems;
    }
}
