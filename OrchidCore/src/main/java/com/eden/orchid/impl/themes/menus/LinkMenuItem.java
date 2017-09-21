package com.eden.orchid.impl.themes.menus;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import com.eden.orchid.api.theme.pages.OrchidExternalPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class LinkMenuItem implements OrchidMenuItemFactory {

    private OrchidContext context;

    @Option
    public String title;

    @Option
    public String url;

    @Inject
    public LinkMenuItem(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String getKey() {
        return "link";
    }

    @Override
    public List<OrchidMenuItem> getMenuItems() {
        List<OrchidMenuItem> menuItems = new ArrayList<>();

        if (!EdenUtils.isEmpty(title) && !EdenUtils.isEmpty(url)) {
            if(url.trim().equals("/")) {
                url = context.getBaseUrl();
            }
            else if (!(url.startsWith("http://") || url.startsWith("https://"))) {
                url = OrchidUtils.applyBaseUrl(context, url);
            }

            menuItems.add(new OrchidMenuItem(context, new OrchidExternalPage(OrchidReference.fromUrl(context, title, url))));
        }

        return menuItems;
    }
}
