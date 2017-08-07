package com.eden.orchid.impl.themes.menus;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class IndexMenuItem implements OrchidMenuItemFactory {

    private OrchidContext context;

    @Inject
    public IndexMenuItem(OrchidContext context) {
        this.context = context;
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        List<OrchidMenuItem> menuItems = new ArrayList<>();
        if (menuItemJson.has("title") && menuItemJson.has("index")) {
            String title = menuItemJson.getString("title");
            String index = menuItemJson.getString("index");
            OrchidIndex foundIndex = context.getIndex().findIndex(index);
            menuItems.add(new OrchidMenuItem(context, title, foundIndex));
        }

        return menuItems;
    }
}
