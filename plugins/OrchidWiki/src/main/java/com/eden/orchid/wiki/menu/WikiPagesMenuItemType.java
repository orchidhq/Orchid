package com.eden.orchid.wiki.menu;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.theme.menus.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.OrchidMenuItemType;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.wiki.WikiPage;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WikiPagesMenuItemType implements OrchidMenuItemType {

    private OrchidContext context;

    @Inject
    public WikiPagesMenuItemType(OrchidContext context) {
        this.context = context;
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        List<OrchidMenuItem> menuItems = new ArrayList<>();

        OrchidIndex wikiIndex = context.getIndex().get("wiki");

        Map<String, OrchidIndex> wikiSections = wikiIndex.getChildren();

        for (Map.Entry<String, OrchidIndex> section : wikiSections.entrySet()) {
            List<OrchidPage> sectionPages = section.getValue().getAllPages();

            sectionPages.sort((OrchidPage o1, OrchidPage o2) -> {
                if (o1 instanceof WikiPage && o2 instanceof WikiPage) {
                    return ((WikiPage) o1).getOrder() - ((WikiPage) o2).getOrder();
                }
                else {
                    return 0;
                }
            });

            if(sectionPages.size() == 1) {
                menuItems.add(new OrchidMenuItem(context, sectionPages.get(0)));
            }
            else {
                menuItems.add(new OrchidMenuItem(context, section.getKey(), sectionPages));
            }

        }

        menuItems.sort((OrchidMenuItem o1, OrchidMenuItem o2) -> {
            OrchidPage p1 = (o1.isHasChildren()) ? o1.getChildren().get(0).getPage() : o1.getPage();
            OrchidPage p2 = (o2.isHasChildren()) ? o2.getChildren().get(0).getPage() : o2.getPage();

            if ((p1 != null && p1 instanceof WikiPage) && (p2 != null && p2 instanceof WikiPage)) {
                return ((WikiPage) p1).getOrder() - ((WikiPage) p2).getOrder();
            }
            else {
                return 0;
            }
        });


        return menuItems;
    }
}
