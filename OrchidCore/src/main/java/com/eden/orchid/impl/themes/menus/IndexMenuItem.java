package com.eden.orchid.impl.themes.menus;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class IndexMenuItem implements OrchidMenuItemFactory {

    private OrchidContext context;

    @Option
    public String title;

    @Option
    public String index;

    @Inject
    public IndexMenuItem(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String getKey() {
        return "index";
    }

    @Override
    public List<OrchidMenuItem> getMenuItems() {
        List<OrchidMenuItem> menuItems = new ArrayList<>();
        if (!EdenUtils.isEmpty(title) && !EdenUtils.isEmpty(index)) {
            OrchidIndex foundIndex = context.getIndex().findIndex(index);
            menuItems.add(new OrchidMenuItem(context, title, foundIndex));
        }

        return menuItems;
    }
}
