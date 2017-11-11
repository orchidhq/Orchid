package com.eden.orchid.pages.menu;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PagesMenuType extends OrchidMenuItem {

    @Option
    public boolean atRoot;

    @Option
    public String section;

    @Option
    public String title;

    @Inject
    public PagesMenuType(OrchidContext context) {
        super(context, "pages", 100);
    }

    @Override
    public List<OrchidMenuItemImpl> getMenuItems() {
        List<OrchidMenuItemImpl> menuItems = new ArrayList<>();

        List<OrchidPage> allPages = context.getInternalIndex().getGeneratorPages("pages");

        List<OrchidPage> pages = (EdenUtils.isEmpty(section))
                ? allPages
                : allPages
                        .stream()
                        .filter(page -> page.getReference().getPath().startsWith(section))
                        .collect(Collectors.toList());

        if(atRoot) {
            for(OrchidPage page : pages) {
                menuItems.add(new OrchidMenuItemImpl(context, page));
            }
        }
        else {
            if(EdenUtils.isEmpty(title)) {
                if(!EdenUtils.isEmpty(section)) {
                    title = StringUtils.capitalize(section);
                }
                else {
                    title = "Pages";
                }
            }
            menuItems.add(new OrchidMenuItemImpl(context, title, pages));
        }

        for (OrchidMenuItemImpl item : menuItems) {
            item.setIndexComparator(menuItemComparator);
        }

        return menuItems;
    }

    Comparator<OrchidMenuItemImpl> menuItemComparator = (OrchidMenuItemImpl o1, OrchidMenuItemImpl o2) -> {
        String o1Title = "";
        String o2Title = "";

        if (o1.getPage() != null) {
            o1Title = o1.getPage().getTitle();
        }
        else if (o1.isHasChildren() && o1.getChildren().size() > 0 && o1.getChildren().get(0) != null && o1.getChildren().get(0).getPage() != null) {
            o1Title = o1.getChildren().get(0).getTitle();
        }

        if (o2.getPage() != null) {
            o2Title = o2.getPage().getTitle();
        }
        else if (o2.isHasChildren() && o2.getChildren().size() > 0 && o2.getChildren().get(0) != null && o2.getChildren().get(0).getPage() != null) {
            o2Title = o2.getChildren().get(0).getTitle();
        }
        return o1Title.compareTo(o2Title);
    };
}
