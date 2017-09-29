package com.eden.orchid.javadoc.menu;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.javadoc.JavadocGenerator;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AllPackagesMenuItemType extends OrchidMenuItem {

    @Inject
    public AllPackagesMenuItemType(OrchidContext context) {
        super(context, "javadocPackages", 100);
    }

    @Override
    public List<OrchidMenuItemImpl> getMenuItems() {
        List<OrchidMenuItemImpl> items = new ArrayList<>();
        if(JavadocGenerator.allPackages != null) {
            List<OrchidPage> pages = new ArrayList<>(JavadocGenerator.allPackages);
            pages.sort(Comparator.comparing(OrchidPage::getTitle));
            items.add(new OrchidMenuItemImpl(context, "All Packages", pages));
        }
        return items;
    }
}
