package com.eden.orchid.impl.themes.menus;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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

        OrchidPage page = context.findPage(collectionType, collectionId, itemId);

        if(page != null) {
            OrchidMenuItemImpl item = new OrchidMenuItemImpl(context, page);
            if(!EdenUtils.isEmpty(title)) {
                item.setTitle(title);
            }
            menuItems.add(item);
        }

        return menuItems;
    }
}
