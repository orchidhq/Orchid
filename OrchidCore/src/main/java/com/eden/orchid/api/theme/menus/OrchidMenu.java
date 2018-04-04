package com.eden.orchid.api.theme.menus;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.server.annotations.Extensible;
import com.eden.orchid.api.theme.components.ModularList;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

@Extensible
@Getter @Setter
public final class OrchidMenu extends ModularList<OrchidMenu, OrchidMenuItem> {

    public OrchidMenu(OrchidContext context, JSONArray menuItems) {
        super(context, OrchidMenuItem.class, menuItems);
    }

    public List<OrchidMenuItemImpl> getMenuItems(OrchidPage containingPage) {
        ArrayList<OrchidMenuItemImpl> menuItemsChildren = new ArrayList<>();
        for (OrchidMenuItem menuItem : get(containingPage)) {
            menuItemsChildren.addAll(menuItem.getMenuItems());
        }

        return menuItemsChildren;
    }

}
