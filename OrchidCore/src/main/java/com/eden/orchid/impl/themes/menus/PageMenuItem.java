package com.eden.orchid.impl.themes.menus;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public final class PageMenuItem extends OrchidMenuItem {

    @Option
    @Description("The title of this menu item")
    public String title;

    @Option
    @Description("The Id of an item to look up.")
    private String itemId;

    @Option
    @Description("The type of collection the item is expected to come from.")
    private String collectionType;

    @Option
    @Description("The specific Id of the given collection type where the item is expected to come from.")
    private String collectionId;

    @Inject
    public PageMenuItem(OrchidContext context) {
        super(context, "page", 100);
    }

    @Override
    public List<OrchidMenuItemImpl> getMenuItems() {
        List<OrchidMenuItemImpl> menuItems = new ArrayList<>();

        Object page;

        if(!EdenUtils.isEmpty(collectionId) && !EdenUtils.isEmpty(collectionType) && !EdenUtils.isEmpty(itemId)) {
            page = context.findInCollection(collectionType, collectionId, itemId);
        }
        else if(!EdenUtils.isEmpty(collectionType) && !EdenUtils.isEmpty(itemId)) {
            page = context.findInCollection(collectionType, itemId);
        }
        else if(!EdenUtils.isEmpty(itemId)) {
            page = context.findInCollection(itemId);
        }
        else {
            page = null;
        }

        if(page != null && page instanceof OrchidPage) {
            OrchidMenuItemImpl item = new OrchidMenuItemImpl(context, (OrchidPage) page);
            if(!EdenUtils.isEmpty(title)) {
                item.setTitle(title);
            }
            menuItems.add(item);
        }

        return menuItems;
    }
}
