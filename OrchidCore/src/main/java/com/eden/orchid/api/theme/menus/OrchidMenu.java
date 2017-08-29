package com.eden.orchid.api.theme.menus;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter @Setter
public final class OrchidMenu {

    private OrchidContext context;
    private JSONArray menuJson;
    private Set<OrchidMenuItemFactory> menuItemTypes;

    private List<OrchidMenuItem> menuItems;

    public OrchidMenu(OrchidContext context, JSONArray menuItems) {
        this.context = context;
        this.menuJson = menuItems;
    }

    public List<OrchidMenuItem> getMenuItems() {
        if (menuItems == null) {
            menuItems = new ArrayList<>();

            menuItemTypes = OrchidUtils.resolveSet(context, OrchidMenuItemFactory.class);
            for (OrchidMenuItemFactory factory : menuItemTypes) {
                Clog.i("{} - {}", factory.getKey(), factory.getClass().getName());
            }

            for (int i = 0; i < menuJson.length(); i++) {
                JSONObject menuItemJson = menuJson.getJSONObject(i);
                String menuItemType = menuItemJson.getString("type");
                Clog.v("Creating menu item: {}", menuItemType);

                for (OrchidMenuItemFactory factory : menuItemTypes) {
                    if(factory.getKey().equals(menuItemType)) {
                        Clog.v("Found menu item: {}", menuItemType);
                        menuItems.addAll(factory.getMenuItems(menuItemJson));
                        break;
                    }
                }
            }
        }

        return menuItems;
    }

    public void addMenuItem(JSONObject menuItemJson) {
        menuItems = null;
        menuJson.put(menuItemJson);
    }

    public void addMenuItems(JSONArray menuItemsJson) {
        menuItems = null;
        for (int i = 0; i < menuItemsJson.length(); i++) {
            menuJson.put(menuItemsJson.get(i));
        }
    }
}
