package com.eden.orchid.api.theme.menus.menuItem;

import org.json.JSONObject;

import java.util.List;

public interface OrchidMenuItemFactory {

    String getKey();

    List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson);

}
