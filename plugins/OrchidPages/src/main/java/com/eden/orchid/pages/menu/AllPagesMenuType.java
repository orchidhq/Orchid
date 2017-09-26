package com.eden.orchid.pages.menu;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AllPagesMenuType extends OrchidMenuItem {

    @Option
    public boolean atRoot;

    @Inject
    public AllPagesMenuType(OrchidContext context) {
        super(context, "pages", 100);
    }

    @Override
    public List<OrchidMenuItemImpl> getMenuItems() {
        List<OrchidMenuItemImpl> menuItems = new ArrayList<>();

        List<OrchidPage> pages = context.getInternalIndex().getGeneratorPages("pages");

        if(atRoot) {
            for(OrchidPage page : pages) {
                menuItems.add(new OrchidMenuItemImpl(context, page));
            }
        }
        else {
            menuItems.add(new OrchidMenuItemImpl(context, "Pages", pages));
        }

        return menuItems;
    }
}
