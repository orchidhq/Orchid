package com.eden.orchid.wiki.menu;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.indexing.OrchidInternalIndex;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.utilities.OrchidUtils;
import com.eden.orchid.wiki.model.WikiModel;
import com.eden.orchid.wiki.model.WikiSection;
import com.eden.orchid.wiki.pages.WikiPage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
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
            for (OrchidPage page : sectionPages) {
                OrchidReference ref = new OrchidReference(page.getReference());
                if(!menuSection.equalsIgnoreCase("wiki")) {
                    ref.stripFromPath("wiki");
                }
                wikiPagesIndex.addToIndex(ref.getPath(), page);
            }
        }

        menuItems.add(new OrchidMenuItemImpl(context, menuItemTitle, wikiPagesIndex));

        for(OrchidMenuItemImpl item : menuItems) {
            item.setIndexComparator(menuItemComparator);
        }

        return menuItems;
    }

    Comparator<OrchidMenuItemImpl> menuItemComparator = (OrchidMenuItemImpl o1, OrchidMenuItemImpl o2) -> {
        int o1Order = 0;
        int o2Order = 0;

        if(o1.getPage() != null) {
            if(o1.getPage() instanceof WikiPage) {
                WikiPage oo1 = (WikiPage) o1.getPage();
                o1Order = oo1.getOrder();
            }
        }
        if(o2.getPage() != null) {
            if(o2.getPage() instanceof WikiPage) {
                WikiPage oo2 = (WikiPage) o2.getPage();
                o2Order = oo2.getOrder();
            }
        }

        if(o1Order > 0 && o2Order > 0) {
            return o1Order - o2Order;
        }
        else if(o1.isHasChildren()){
            return 1;
        }
        else if(o2.isHasChildren()){
            return -1;
        }
        else {
            return 0;
        }
    };
}
