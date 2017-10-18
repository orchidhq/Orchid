package com.eden.orchid.pages.menu;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PageMenuType extends OrchidMenuItem {

    @Option("page")
    public String pageName;

    @Inject
    public PageMenuType(OrchidContext context) {
        super(context, "page", 100);
    }

    @Override
    public List<OrchidMenuItemImpl> getMenuItems() {
        List<OrchidMenuItemImpl> menuItems = new ArrayList<>();

        List<OrchidPage> pages = context.getInternalIndex().getGeneratorPages("pages");

        if(!EdenUtils.isEmpty(pageName)) {
            for (OrchidPage page : pages) {
                if(page.getTitle().equals(pageName)) {
                    menuItems.add(new OrchidMenuItemImpl(context, page));
                }
            }
        }

        return menuItems;
    }
}
