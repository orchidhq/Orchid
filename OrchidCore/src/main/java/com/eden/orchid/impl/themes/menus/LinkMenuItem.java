package com.eden.orchid.impl.themes.menus;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.pages.OrchidExternalPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public final class LinkMenuItem extends OrchidMenuItem {

    @Option
    public String title;

    @Option
    public String subtitle;

    @Option
    public String url;

    @Inject
    public LinkMenuItem(OrchidContext context) {
        super(context, "link", 100);
    }

    @Override
    public List<OrchidMenuItemImpl> getMenuItems() {
        List<OrchidMenuItemImpl> menuItems = new ArrayList<>();

        if (!EdenUtils.isEmpty(title) && !EdenUtils.isEmpty(url)) {
            if(url.trim().equals("/")) {
                url = context.getBaseUrl();
            }
            else if (!(OrchidUtils.isExternal(url))) {
                url = OrchidUtils.applyBaseUrl(context, url);
            }

            OrchidMenuItemImpl item = new OrchidMenuItemImpl(context, new OrchidExternalPage(OrchidReference.fromUrl(context, title, url)));

            if(!EdenUtils.isEmpty(subtitle)) {
                item.setSubtitle(subtitle);
            }

            menuItems.add(item);
        }

        return menuItems;
    }
}
