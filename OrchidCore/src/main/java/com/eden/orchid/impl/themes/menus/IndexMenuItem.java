package com.eden.orchid.impl.themes.menus;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public final class IndexMenuItem extends OrchidMenuItem {

    @Option
    public String title;

    @Option
    public String index;

    @Inject
    public IndexMenuItem(OrchidContext context) {
        super(context, "index", 100);
    }

    @Override
    public List<OrchidMenuItemImpl> getMenuItems() {
        List<OrchidMenuItemImpl> menuItems = new ArrayList<>();
        if (!EdenUtils.isEmpty(title) && !EdenUtils.isEmpty(index)) {
            OrchidIndex foundIndex = context.getIndex().findIndex(index);
            menuItems.add(new OrchidMenuItemImpl(context, title, foundIndex));
        }

        return menuItems;
    }
}
