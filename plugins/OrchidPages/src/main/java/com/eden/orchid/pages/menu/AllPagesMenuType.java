package com.eden.orchid.pages.menu;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AllPagesMenuType implements OrchidMenuItemFactory {

    protected OrchidContext context;

    @Option
    public boolean atRoot;

    @Inject
    public AllPagesMenuType(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String getKey() {
        return "pages";
    }

    @Override
    public List<OrchidMenuItem> getMenuItems() {
        List<OrchidMenuItem> menuItems = new ArrayList<>();

        List<OrchidPage> pages = context.getInternalIndex().getGeneratorPages("pages");

        if(atRoot) {
            for(OrchidPage page : pages) {
                menuItems.add(new OrchidMenuItem(context, page));
            }
        }
        else {
            menuItems.add(new OrchidMenuItem(context, "Pages", pages));
        }

        return menuItems;
    }
}
