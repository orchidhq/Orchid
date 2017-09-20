package com.eden.orchid.pages.menu;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PageMenuType implements OrchidMenuItemFactory {

    protected OrchidContext context;

    @Option("page")
    public String pageName;

    @Inject
    public PageMenuType(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String getKey() {
        return "page";
    }

    @Override
    public List<OrchidMenuItem> getMenuItems() {
        List<OrchidMenuItem> menuItems = new ArrayList<>();

        List<OrchidPage> pages = context.getInternalIndex().getGeneratorPages("pages");

        if(!EdenUtils.isEmpty(pageName)) {
            for (OrchidPage page : pages) {
                if(page.getTitle().equals(pageName)) {
                    menuItems.add(new OrchidMenuItem(context, page));
                }
            }
        }

        return menuItems;
    }
}
