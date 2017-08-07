package com.eden.orchid.api.theme.menus.menuItem;

import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import org.json.JSONObject;

import java.util.List;

public interface OrchidMenuItemFactory {

    List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson);

}
