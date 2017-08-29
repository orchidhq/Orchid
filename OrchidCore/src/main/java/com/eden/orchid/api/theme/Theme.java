package com.eden.orchid.api.theme;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsExtractor;
import com.eden.orchid.api.theme.menus.MenuHolder;
import com.eden.orchid.api.theme.menus.MenuHolderDelegate;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.List;

public abstract class Theme extends AbstractTheme implements MenuHolder {

    private JSONObject themeOptions;
    protected MenuHolder menus;

    @Inject
    public Theme(OrchidContext context, int priority) {
        super(context, priority);
        this.menus = new MenuHolderDelegate(context);
    }

    public void clearCache() {
        super.clearCache();
        menus.clearMenus();
        themeOptions = null;
    }

    @Override
    public void extractOptions(OrchidContext context, JSONObject options) {
        this.themeOptions = options;
        OptionsExtractor extractor = context.getInjector().getInstance(OptionsExtractor.class);
        extractor.extractOptions(this, options);
    }

    protected void createMenus() {
        JSONElement menuElement = null;

        // look for menus in local theme options
        if(themeOptions != null) {
            menuElement = new JSONElement(themeOptions).query("menu");
            if(menuElement != null) {
                Clog.v("We have local theme options");
            }
        }

        // look for menus in global site options
        if(menuElement == null) {
            menuElement = context.query("menu");
            if(menuElement != null) {
                Clog.v("We have global theme options");
            }
        }

        if(menuElement != null) {
            // create single menu from array definition
            if (OrchidUtils.elementIsArray(menuElement)) {
                Clog.v("We have menu array");
                createMenu(null, (JSONArray) menuElement.getElement());
                Clog.v(((JSONArray) menuElement.getElement()).toString(2));
            }

            // create multiple menus from object definition
            else if (OrchidUtils.elementIsObject(menuElement)) {
                Clog.v("We have menu object");
                Clog.v(((JSONObject) menuElement.getElement()).toString(2));
                createMenus((JSONObject) menuElement.getElement());
            }
        }
    }

// Get delegates
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public MenuHolder getMenuHolder() {
        return menus;
    }

    public List<OrchidMenuItem> getMenu() {
        return menus.getMenu();
    }

    public List<OrchidMenuItem> getMenu(String menuId) {
        return menus.getMenu(menuId);
    }
}
