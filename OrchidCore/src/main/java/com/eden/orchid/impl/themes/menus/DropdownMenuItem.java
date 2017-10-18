package com.eden.orchid.impl.themes.menus;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.menus.OrchidMenu;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public final class DropdownMenuItem extends OrchidMenuItem {

    @Option
    public String title;

    @Option
    public String subtitle;

    @Option
    public OrchidMenu menu;

    @Inject
    public DropdownMenuItem(OrchidContext context) {
        super(context, "dropdown", 100);
    }

    @Override
    public List<OrchidMenuItemImpl> getMenuItems() {
        List<OrchidMenuItemImpl> menuItems = new ArrayList<>();

        if (!EdenUtils.isEmpty(title) && menu != null) {

            List<OrchidMenuItemImpl> dropdownItems = menu.getMenuItems();

            OrchidMenuItemImpl item = new OrchidMenuItemImpl(context, dropdownItems, title);

            if(!EdenUtils.isEmpty(subtitle)) {
                item.setSubtitle(subtitle);
            }
            menuItems.add(item);
        }

        return menuItems;
    }
}
