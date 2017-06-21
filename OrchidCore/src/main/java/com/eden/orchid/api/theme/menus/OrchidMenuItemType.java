package com.eden.orchid.api.theme.menus;

import org.json.JSONObject;

import java.util.List;

public interface OrchidMenuItemType {

    List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson);

}
