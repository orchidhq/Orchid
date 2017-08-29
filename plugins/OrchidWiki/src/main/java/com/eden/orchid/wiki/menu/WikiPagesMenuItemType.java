package com.eden.orchid.wiki.menu;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.impl.indexing.OrchidInternalIndex;
import com.eden.orchid.utilities.OrchidUtils;
import com.eden.orchid.wiki.WikiGenerator;
import com.eden.orchid.wiki.WikiPage;
import com.eden.orchid.wiki.WikiSummaryPage;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WikiPagesMenuItemType implements OrchidMenuItemFactory {

    private OrchidContext context;

    @Inject
    public WikiPagesMenuItemType(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String getKey() {
        return "wiki";
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        List<OrchidMenuItem> menuItems = new ArrayList<>();

        Map<String, EdenPair<WikiSummaryPage, List<WikiPage>>> sections = new HashMap<>();

        String menuItemTitle;
        String menuSection;

        if (menuItemJson.has("section") && WikiGenerator.sections.containsKey(menuItemJson.getString("section"))) {
            sections.put(menuItemJson.getString("section"), WikiGenerator.sections.get(menuItemJson.getString("section")));
            menuItemTitle = OrchidUtils.camelcaseToTitleCase(menuItemJson.getString("section")) + " Wiki";
            menuSection = menuItemJson.getString("section");
        }
        else {
            sections.putAll(WikiGenerator.sections);
            menuItemTitle = "Wiki";
            menuSection = "wiki";
        }

        OrchidIndex wikiPagesIndex = new OrchidInternalIndex(menuSection);

        for (Map.Entry<String, EdenPair<WikiSummaryPage, List<WikiPage>>> section : sections.entrySet()) {
            List<OrchidPage> sectionPages = new ArrayList<>(section.getValue().second);

            sectionPages.sort((OrchidPage o1, OrchidPage o2) -> {
                if (o1 instanceof WikiPage && o2 instanceof WikiPage) {
                    return ((WikiPage) o1).getOrder() - ((WikiPage) o2).getOrder();
                }
                else {
                    return 0;
                }
            });

            for (OrchidPage page : sectionPages) {
                OrchidReference ref = new OrchidReference(context, page.getReference());
                if(!menuSection.equalsIgnoreCase("wiki")) {
                    ref.stripFromPath("wiki");
                }
                wikiPagesIndex.addToIndex(ref.getPath(), page);
            }
        }

        menuItems.add(new OrchidMenuItem(context, menuItemTitle, wikiPagesIndex));

        return menuItems;
    }
}
