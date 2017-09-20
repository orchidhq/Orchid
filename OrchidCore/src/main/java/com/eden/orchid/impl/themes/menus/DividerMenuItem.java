package com.eden.orchid.impl.themes.menus;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class DividerMenuItem implements OrchidMenuItemFactory {

    private OrchidContext context;

    @Option
    public String title;

    @Inject
    public DividerMenuItem(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String getKey() {
        return "separator";
    }

    @Override
    public List<OrchidMenuItem> getMenuItems() {
        List<OrchidMenuItem> menuItems = new ArrayList<>();

        if(!EdenUtils.isEmpty(title)) {
            menuItems.add(new OrchidMenuItem(context, title));
        }
        else {
            menuItems.add(new OrchidMenuItem(context));
        }

        return menuItems;
    }
}
