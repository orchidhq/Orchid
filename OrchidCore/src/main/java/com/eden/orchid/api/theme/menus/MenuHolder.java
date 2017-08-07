package com.eden.orchid.api.theme.menus;

import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface MenuHolder {

    void createMenus(JSONObject menuJson);
    void createMenu(String menuId, JSONArray menuJson);

    void addMenuItem(String menuId, JSONObject menuItemJson);
    void addMenuItems(String menuId, JSONArray menuItemsJson);

    List<OrchidMenuItem> getMenu();
    List<OrchidMenuItem> getMenu(String menuId);

    void clearMenus();
}
