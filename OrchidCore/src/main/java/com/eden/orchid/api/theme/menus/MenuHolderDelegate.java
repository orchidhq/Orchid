package com.eden.orchid.api.theme.menus;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuHolderDelegate implements MenuHolder {

    protected OrchidContext context;
    protected Map<String, OrchidMenu> menus;

    @Inject
    public MenuHolderDelegate(OrchidContext context) {
        this.context = context;
        menus = new HashMap<>();
    }

    @Override
    public MenuHolder getMenuHolder() {
        return this;
    }

    @Override
    public void createMenus(JSONObject menuJson) {
        for (String key : menuJson.keySet()) {
            if(menuJson.get(key) instanceof JSONArray) {
                createMenu(key, menuJson.getJSONArray(key));
            }
        }
    }

    @Override
    public void createMenu(String menuId, JSONArray menuJson) {
        if(!menus.containsKey(menuId)) {
            menus.put(menuId, new OrchidMenu(context, menuJson));
        }
    }

    @Override
    public void addMenuItem(String menuId, JSONObject menuItemJson) {
        if(!menus.containsKey(menuId)) {
            menus.put(menuId, new OrchidMenu(context, new JSONArray()));
        }

        menus.get(menuId).addMenuItem(menuItemJson);
    }

    @Override
    public void addMenuItems(String menuId, JSONArray menuItemsJson) {
        if(!menus.containsKey(menuId)) {
            menus.put(menuId, new OrchidMenu(context, new JSONArray()));
        }

        menus.get(menuId).addMenuItems(menuItemsJson);
    }

    @Override
    public List<OrchidMenuItem> getMenu() {
        return getMenu(null);
    }

    @Override
    public List<OrchidMenuItem> getMenu(String menuId) {
        if (menus.containsKey(menuId)) {
            return menus.get(menuId).getMenuItems();
        }

        return new ArrayList<>();
    }

    @Override
    public void clearMenus() {
        menus.clear();
    }

}
