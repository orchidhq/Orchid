package com.eden.orchid.javadoc.menu;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.javadoc.JavadocGenerator;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AllClassesMenuItemType implements OrchidMenuItemFactory {

    private OrchidContext context;

    @Inject
    public AllClassesMenuItemType(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String getKey() {
        return "javadocClasses";
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        List<OrchidMenuItem> items = new ArrayList<>();
        List<OrchidPage> pages = new ArrayList<>(JavadocGenerator.allClasses);
        pages.sort(Comparator.comparing(OrchidPage::getTitle));
        items.add(new OrchidMenuItem(context, "All Classes", pages));
        return items;
    }
}
