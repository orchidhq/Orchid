package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.api.theme.menus.OrchidMenu;
import com.eden.orchid.api.theme.menus.OrchidMenuItem;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Theme extends DefaultResourceSource {

    protected Map<String, OrchidMenu> menus;

    @Inject
    public Theme(OrchidContext context) {
        super(context);

        this.menus = new HashMap<>();
    }

    public String scripts() {
        String scripts = "<!-- start:inject scripts -->";

        for(OrchidPage script : context.getIndex().find("assets/js")) {
            scripts += "<script src=\"" + script.getLink() + "\"></script>";
        }

        scripts += "<!-- end:inject scripts -->";

        return scripts;
    }

    public String styles() {
        String styles = "<!-- start:inject styles -->";

        for(OrchidPage style : context.getIndex().find("assets/css")) {
            styles += "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + style.getLink() + "\"/>";
        }

        styles += "<!-- end:inject styles -->";

        return styles;
    }


    public List<OrchidMenuItem> menu() {
        return menu(null);
    }

    public List<OrchidMenuItem> menu(String menuPosition) {
        if(!this.menus.containsKey(menuPosition)) {
            this.menus.put(menuPosition, new OrchidMenu(context, menuPosition));
        }

        OrchidMenu menu = this.menus.get(menuPosition);
        if(menu != null) {
            return menu.getMenuItems();
        }
        else {
            return new ArrayList<>();
        }
    }

    public void clearCachedMenus() {
        menus.clear();
    }
}
