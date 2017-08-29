package com.eden.orchid.api.theme.menus;

import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface MenuHolder {

    MenuHolder getMenuHolder();

    default void createMenus(JSONObject menuJson) {
        getMenuHolder().createMenus(menuJson);
    }

    default void createMenu(String menuId, JSONArray menuJson) {
        getMenuHolder().createMenu(menuId, menuJson);
    }

    default void addMenuItem(String menuId, JSONObject menuItemJson) {
        getMenuHolder().addMenuItem(menuId, menuItemJson);
    }

    default void addMenuItems(String menuId, JSONArray menuItemsJson) {
        getMenuHolder().addMenuItems(menuId, menuItemsJson);
    }

    default List<OrchidMenuItem> getMenu() {
        return getMenuHolder().getMenu();
    }

    default List<OrchidMenuItem> getMenu(String menuId) {
        return getMenuHolder().getMenu(menuId);
    }

    default void clearMenus() {
        getMenuHolder().clearMenus();
    }
}
