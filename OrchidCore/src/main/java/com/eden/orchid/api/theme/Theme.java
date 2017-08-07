package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetHolderDelegate;
import com.eden.orchid.api.theme.menus.MenuHolder;
import com.eden.orchid.api.theme.menus.MenuHolderDelegate;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.List;

public abstract class Theme extends DefaultResourceSource implements AssetHolder, MenuHolder {

    protected AssetHolder assets;
    protected MenuHolder menus;

    @Inject
    public Theme(OrchidContext context) {
        super(context);
//        this.assets = context.getInjector().getInstance(AssetHolderDelegate.class);
//        this.menus = context.getInjector().getInstance(MenuHolderDelegate.class);

        this.assets = new AssetHolderDelegate();
        this.menus = new MenuHolderDelegate(context);
    }

    public void clearCache() {
        menus.clearMenus();
        assets.clearAssets();
    }

// Assets
//----------------------------------------------------------------------------------------------------------------------

    @Override public void addJs(OrchidPage jsAsset) { assets.addJs(jsAsset); }
    @Override public void addCss(OrchidPage cssAsset) { assets.addCss(cssAsset); }
    @Override public List<OrchidPage> getScripts() { return assets.getScripts(); }
    @Override public List<OrchidPage> getStyles() { return assets.getStyles(); }
    @Override public void flushJs() { assets.flushJs(); }
    @Override public void flushCss() { assets.flushCss(); }
    @Override public void clearAssets() { assets.clearAssets(); }

// Menus
//----------------------------------------------------------------------------------------------------------------------

    @Override public void createMenus(JSONObject menuJson) { menus.createMenus(menuJson); }
    @Override public void createMenu(String menuId, JSONArray menuJson) { menus.createMenu(menuId, menuJson); }
    @Override public void addMenuItem(String menuId, JSONObject menuItemJson) { menus.addMenuItem(menuId, menuItemJson); }
    @Override public void addMenuItems(String menuId, JSONArray menuItemsJson) { menus.addMenuItems(menuId, menuItemsJson); }
    @Override public List<OrchidMenuItem> getMenu() { return menus.getMenu(); }
    @Override public List<OrchidMenuItem> getMenu(String menuId) { return menus.getMenu(menuId); }
    @Override public void clearMenus() { menus.clearMenus(); }

}
