package com.eden.orchid.kss.menu;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.kss.KssGenerator;
import com.eden.orchid.kss.KssPage;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StyleguidePagesMenuItemType implements OrchidMenuItemFactory {

    private OrchidContext context;

    @Inject
    public StyleguidePagesMenuItemType(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String getKey() {
        return "styleguide";
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        List<OrchidMenuItem> menuItems = new ArrayList<>();

        Map<String, List<KssPage>> sections = new HashMap<>();

        String menuItemTitle;
        String menuSection;

        if (menuItemJson.has("section") && KssGenerator.sections.containsKey(menuItemJson.getString("section"))) {
            sections.put(menuItemJson.getString("section"), KssGenerator.sections.get(menuItemJson.getString("section")));
            menuItemTitle = OrchidUtils.camelcaseToTitleCase(menuItemJson.getString("section")) + " Styleguide";
            menuSection = menuItemJson.getString("section");
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

        menuItems.add(new OrchidMenuItem(context, menuItemTitle, allPages));

        return menuItems;
    }
}
