package com.eden.orchid.kss.menu;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.kss.KssGenerator;
import com.eden.orchid.kss.KssPage;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StyleguidePagesMenuItemType extends OrchidMenuItem {

    @Option
    public String section;

    @Inject
    public StyleguidePagesMenuItemType(OrchidContext context) {
        super(context, "styleguide", 100);
    }

    @Override
    public List<OrchidMenuItemImpl> getMenuItems() {
        List<OrchidMenuItemImpl> menuItems = new ArrayList<>();

        Map<String, List<KssPage>> sections = new HashMap<>();

        String menuItemTitle;
        String menuSection;

        if (!EdenUtils.isEmpty(section) && KssGenerator.sections.containsKey(section)) {
            sections.put(section, KssGenerator.sections.get(section));
            menuItemTitle = OrchidUtils.camelcaseToTitleCase(section) + " Styleguide";
            menuSection = section;
        }
        else {
            sections.putAll(KssGenerator.sections);
            menuItemTitle = "Styleguide";
            menuSection = "styleguide";
        }

        List<OrchidPage> allPages = new ArrayList<>();

        for (List<KssPage> sectionPages : sections.values()) {
            sectionPages.sort((KssPage o1, KssPage o2) -> {
                int comparableSections = Math.min(o1.getSectionPath().length, o2.getSectionPath().length);
                for (int i = 0; i < comparableSections; i++) {
                    if(o1.getSectionPath()[i] != o2.getSectionPath()[i]) {
                        return o1.getSectionPath()[i] - o2.getSectionPath()[i];
                    }
                }
                return o1.getSectionPath().length - o2.getSectionPath().length;
            });

            allPages.addAll(sectionPages);
        }

        menuItems.add(new OrchidMenuItemImpl(context, menuItemTitle, allPages));

        return menuItems;
    }
}
