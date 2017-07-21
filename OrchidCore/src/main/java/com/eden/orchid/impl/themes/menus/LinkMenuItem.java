package com.eden.orchid.impl.themes.menus;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.OrchidMenuItemType;
import com.eden.orchid.api.theme.pages.OrchidExternalPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.name.Named;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class LinkMenuItem implements OrchidMenuItemType {

    private OrchidContext context;
    private String baseUrl;

    @Inject
    public LinkMenuItem(OrchidContext context, @Named("baseUrl") String baseUrl) {
        this.context = context;
        this.baseUrl = baseUrl;
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        List<OrchidMenuItem> menuItems = new ArrayList<>();

        if (menuItemJson.has("title") && menuItemJson.has("url")) {
            String title = menuItemJson.getString("title");
            String url = menuItemJson.getString("url");

            if(url.trim().equals("/")) {
                url = this.baseUrl;
            }
            else if (!(url.startsWith("http://") || url.startsWith("https://"))) {
                url = OrchidUtils.applyBaseUrl(context, OrchidUtils.normalizePath(url));
            }

            menuItems.add(new OrchidMenuItem(context, new OrchidExternalPage(OrchidReference.fromUrl(context, title, url))));
        }

        return menuItems;
    }
}
