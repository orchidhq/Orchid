package com.eden.orchid.wiki.menu;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.api.indexing.OrchidInternalIndex;
import com.eden.orchid.utilities.OrchidUtils;
import com.eden.orchid.wiki.model.WikiModel;
import com.eden.orchid.wiki.model.WikiSection;
import com.eden.orchid.wiki.pages.WikiPage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WikiPagesMenuItemType extends OrchidMenuItem {

    private final WikiModel model;

    @Option
    public String section;

    @Inject
    public WikiPagesMenuItemType(OrchidContext context, WikiModel model) {
        super(context, "wiki", 100);
        this.model = model;
    }

    @Override
    public List<OrchidMenuItemImpl> getMenuItems() {
        List<OrchidMenuItemImpl> menuItems = new ArrayList<>();

        Map<String, WikiSection> sections = new HashMap<>();

        String menuItemTitle;
        String menuSection;

        if (!EdenUtils.isEmpty(section) && model.getSections().containsKey(section)) {
            sections.put(section, model.getSections().get(section));
            menuItemTitle = OrchidUtils.camelcaseToTitleCase(section) + " Wiki";
            menuSection = section;
        }
        else {
            sections.putAll(model.getSections());
            menuItemTitle = "Wiki";
            menuSection = "wiki";
        }

        OrchidIndex wikiPagesIndex = new OrchidInternalIndex(menuSection);

        for (Map.Entry<String, WikiSection> section : sections.entrySet()) {
            List<OrchidPage> sectionPages = new ArrayList<>(section.getValue().getWikiPages());

            sectionPages.sort((OrchidPage o1, OrchidPage o2) -> {
                if (o1 instanceof WikiPage && o2 instanceof WikiPage) {
                    return ((WikiPage) o1).getOrder() - ((WikiPage) o2).getOrder();
                }
                else {
                    return 0;
                }
            });

            for (OrchidPage page : sectionPages) {
                OrchidReference ref = new OrchidReference(page.getReference());
                if(!menuSection.equalsIgnoreCase("wiki")) {
                    ref.stripFromPath("wiki");
                }
                wikiPagesIndex.addToIndex(ref.getPath(), page);
            }
        }

        menuItems.add(new OrchidMenuItemImpl(context, menuItemTitle, wikiPagesIndex));

        return menuItems;
    }
}
