package com.eden.orchid.impl.themes.menus;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.menus.OrchidMenu;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public final class SubmenuMenuItem extends OrchidMenuItem {

    @Option
    @Description("The text of the root menu item.")
    public String title;

    @Option
    @Description("A new menu to nest under this menu item.")
    public OrchidMenu menu;

    @Inject
    public SubmenuMenuItem(OrchidContext context) {
        super(context, "submenu", 100);
    }

    @Override
    public List<OrchidMenuItemImpl> getMenuItems() {
        List<OrchidMenuItemImpl> menuItems = new ArrayList<>();

        if (!EdenUtils.isEmpty(title) && menu != null) {

            List<OrchidMenuItemImpl> dropdownItems = menu.getMenuItems(this.page);

            OrchidMenuItemImpl item = new OrchidMenuItemImpl(context, dropdownItems, title);

            menuItems.add(item);
        }

        return menuItems;
    }
}
