package com.eden.orchid.posts.menu;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.theme.menus.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.OrchidMenuItemType;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoriesMenuType implements OrchidMenuItemType {

    private OrchidContext context;

    @Inject
    public CategoriesMenuType(OrchidContext context) {
        this.context = context;
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        OrchidIndex archive =  context.getIndex().get("archive");

        List<OrchidMenuItem> items = new ArrayList<>();

        if(context.query("options.posts.categories") != null) {
            List<OrchidPage> pages = new ArrayList<>();
            for(Map.Entry<String, OrchidIndex> index : archive.getChildren().entrySet()) {
                pages.add(index.getValue().getAllPages().get(0));
            }
            items.add(new OrchidMenuItem(context, "Categories", pages));
        }
        else {
            items.add(new OrchidMenuItem(context, archive.getAllPages().get(0)));
        }

        return items;
    }
}
