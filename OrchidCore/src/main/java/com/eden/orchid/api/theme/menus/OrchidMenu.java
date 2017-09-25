package com.eden.orchid.api.theme.menus;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter @Setter
public final class OrchidMenu {

    private OrchidContext context;
    private JSONArray menuJson;
    private Map<String, OrchidMenuItemFactory> menuItemTypesMap;

    private List<OrchidMenuItem> menuItems;

    public OrchidMenu(OrchidContext context, JSONArray menuItems) {
        this.context = context;
        this.menuJson = menuItems;

        Set<OrchidMenuItemFactory> menuItemTypes = context.resolveSet(OrchidMenuItemFactory.class);
        menuItemTypesMap = new HashMap<>();

        for (OrchidMenuItemFactory factory : menuItemTypes) {
            menuItemTypesMap.put(factory.getKey(), factory);
        }
    }

    public boolean isEmpty() {
        if (menuItems != null) {
            if(menuItems.size() > 0) {
                return false;
            }
        }
        else if (menuJson != null) {
            if(menuJson.length() > 0) {
                return false;
            }
        }

        return true;
    }

    public List<OrchidMenuItem> getMenuItems() {
        if (menuItems == null) {
            menuItems = new ArrayList<>();

            for (int i = 0; i < menuJson.length(); i++) {
                JSONObject menuItemJson = menuJson.getJSONObject(i);
                String menuItemType = menuItemJson.getString("type");

                if(menuItemTypesMap.containsKey(menuItemType)) {
                    OrchidMenuItemFactory factory = menuItemTypesMap.get(menuItemType);
                    factory.extractOptions(context, menuItemJson);
                    menuItems.addAll(factory.getMenuItems());
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
