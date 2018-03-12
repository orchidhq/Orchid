package com.eden.orchid.impl.themes.menus;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl;
import com.eden.orchid.api.theme.pages.OrchidExternalPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public final class LinkMenuItem extends OrchidMenuItem {

    @Option
    @Description("The title of this menu item")
    public String title;

    @Option
    @Description("The URL of this menu item")
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

            OrchidReference reference = OrchidReference.fromUrl(context, title, url);
            if(reference != null) {
                menuItems.add(new OrchidMenuItemImpl(context, new OrchidExternalPage(reference)));
            }
        }

        return menuItems;
    }
}
