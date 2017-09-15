package com.eden.orchid.kss.menu;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import com.eden.orchid.kss.KssGenerator;
import com.eden.orchid.kss.KssPage;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StyleguideSectionsMenuItemType implements OrchidMenuItemFactory {

    private OrchidContext context;

    @Inject
    public StyleguideSectionsMenuItemType(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String getKey() {
        return "styleguideSections";
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        List<OrchidMenuItem> menuItems = new ArrayList<>();

        for (Map.Entry<String, List<KssPage>> section : KssGenerator.sections.entrySet()) {
            if(section.getValue().size() > 0) {
                menuItems.add(new OrchidMenuItem(context, section.getValue().get(0)));
            }
        }

        return menuItems;
    }
}
