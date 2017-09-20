package com.eden.orchid.api.theme.menus.menuItem;

import com.eden.orchid.api.options.OptionsHolder;

import java.util.List;

public interface OrchidMenuItemFactory extends OptionsHolder {

    String getKey();

    List<OrchidMenuItem> getMenuItems();

}
