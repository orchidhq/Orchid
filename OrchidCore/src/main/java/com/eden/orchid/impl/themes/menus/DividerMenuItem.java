package com.eden.orchid.impl.themes.menus;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.OrchidMenuItemType;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class DividerMenuItem implements OrchidMenuItemType {

    private OrchidContext context;

    @Inject
    public DividerMenuItem(OrchidContext context) {
        this.context = context;
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        List<OrchidMenuItem> menuItems = new ArrayList<>();

        if(menuItemJson.has("title")) {
            menuItems.add(new OrchidMenuItem(context, menuItemJson.getString("title")));
        }
        else {
            menuItems.add(new OrchidMenuItem(context));
        }

        return menuItems;
    }
}
