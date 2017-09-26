package com.eden.orchid.api.theme.menus;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
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
    private Map<String, Class<? extends OrchidMenuItem>> menuItemTypesMap;

    private List<OrchidMenuItem> menuItems;
    private List<OrchidMenuItemImpl> menuItemsChildren;

    public OrchidMenu(OrchidContext context, JSONArray menuItems) {
        this.context = context;
        this.menuJson = menuItems;

        Set<OrchidMenuItem> menuItemTypes = context.resolveSet(OrchidMenuItem.class);
        menuItemTypesMap = new HashMap<>();

        for (OrchidMenuItem factory : menuItemTypes) {
            menuItemTypesMap.put(factory.getKey(), factory.getClass());
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

    public List<OrchidMenuItemImpl> getMenuItems() {
        if (menuItems == null) {
            menuItems = new ArrayList<>();

            for (int i = 0; i < menuJson.length(); i++) {
                JSONObject menuItemJson = menuJson.getJSONObject(i);
                String menuItemType = menuItemJson.getString("type");

                if(menuItemTypesMap.containsKey(menuItemType)) {
                    OrchidMenuItem menuitem = context.getInjector().getInstance(menuItemTypesMap.get(menuItemType));
                    menuitem.extractOptions(context, menuItemJson);
                    menuItems.add(menuitem);
                }
            }
        }

        if(menuItemsChildren == null) {
            menuItemsChildren = new ArrayList<>();
            for(OrchidMenuItem menuItem : menuItems) {
                menuItemsChildren.addAll(menuItem.getMenuItems());
            }
        }

        return menuItemsChildren;
    }

    public void addMenuItem(JSONObject menuItemJson) {
        menuItems = null;
        menuItemsChildren = null;
        menuJson.put(menuItemJson);
    }

    public void addMenuItems(JSONArray menuItemsJson) {
        menuItems = null;
        menuItemsChildren = null;
        for (int i = 0; i < menuItemsJson.length(); i++) {
            menuJson.put(menuItemsJson.get(i));
        }
    }
}
