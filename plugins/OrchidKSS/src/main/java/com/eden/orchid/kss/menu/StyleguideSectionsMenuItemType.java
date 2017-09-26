package com.eden.orchid.kss.menu;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.kss.KssGenerator;
import com.eden.orchid.kss.KssPage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StyleguideSectionsMenuItemType extends OrchidMenuItem {

    @Inject
    public StyleguideSectionsMenuItemType(OrchidContext context) {
        super(context, "styleguideSections", 100);
    }

    @Override
    public List<OrchidMenuItemImpl> getMenuItems() {
        List<OrchidMenuItemImpl> menuItems = new ArrayList<>();

        for (Map.Entry<String, List<KssPage>> section : KssGenerator.sections.entrySet()) {
            if(section.getValue().size() > 0) {
                menuItems.add(new OrchidMenuItemImpl(context, section.getValue().get(0)));
            }
        }

        return menuItems;
    }
}
