package com.eden.orchid.api.theme.menus;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class OrchidMenu {

    protected OrchidContext context;
    protected List<OrchidMenuItem> menuItems;
    protected String menuName;

    public OrchidMenu(OrchidContext context, String menuName) {
        this.context = context;
        this.menuName = menuName;
    }

    public List<OrchidMenuItem> getMenuItems() {
        if(menuItems == null) {
            menuItems = new ArrayList<>();

            JSONElement menuElement = context.query("options.menu");
            Map<String, OrchidMenuItemType> menuItemTypes = OrchidUtils.resolveMap(context, OrchidMenuItemType.class);

            if(OrchidUtils.elementIsArray(menuElement)) {
                JSONArray menuJson = (JSONArray) menuElement.getElement();

                for (int i = 0; i < menuJson.length(); i++) {
                    JSONObject menuItemJson = menuJson.getJSONObject(i);
                    String menuItemType = menuItemJson.getString("type");

                    if(menuItemTypes.containsKey(menuItemType)) {
                        menuItems.addAll(menuItemTypes.get(menuItemType).getMenuItems(menuItemJson));
                    }
                }
            }
        }

        return menuItems;
    }
}
