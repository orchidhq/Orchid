package com.eden.orchid.api.theme.pages;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetHolderDelegate;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.eden.orchid.api.theme.components.ComponentHolderDelegate;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.menus.MenuHolder;
import com.eden.orchid.api.theme.menus.MenuHolderDelegate;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.impl.themes.components.PageContentComponent;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;

public class OrchidPage implements OptionsHolder, AssetHolder, MenuHolder {

    protected OrchidContext context;

    @Getter @Setter protected OrchidResource resource;
    @Getter @Setter protected OrchidReference reference;
    @Getter @Setter protected OrchidPage next;
    @Getter @Setter protected OrchidPage previous;
    @Getter @Setter protected JSONObject data;
    @Getter @Setter protected String key;

    @Getter @Setter protected boolean isCurrent;

    @Getter @Setter @Option protected String title;
    @Getter @Setter @Option protected String description;
    @Getter @Setter @Option protected String layout;
    @Getter @Setter @Option protected String[] templates;

    @Getter @Setter protected ComponentHolder componentHolder;
    @Getter @Setter protected AssetHolder assets;
    @Getter @Setter protected MenuHolder menus;

    public OrchidPage(OrchidResource resource, String key) {
        this(resource, key, null);
    }

    public OrchidPage(OrchidResource resource, String key, String title) {
        this(resource, key, title, null);
    }

    public OrchidPage(OrchidResource resource, String key, String title, String path) {
        this.context = resource.getContext();
        this.componentHolder = context.getInjector().getInstance(ComponentHolderDelegate.class);
        this.assets = context.getInjector().getInstance(AssetHolderDelegate.class);
        this.menus = context.getInjector().getInstance(MenuHolderDelegate.class);

        this.key = key;
        this.templates = new String[]{"page"};

        this.resource = resource;
        this.reference = new OrchidReference(this.context, resource.getReference());
        this.reference.setExtension(resource.getReference().getOutputExtension());

        if (path != null) {
            this.reference.setPath(path);
        }

        if (resource.getEmbeddedData() != null && resource.getEmbeddedData().getElement() instanceof JSONObject) {
            this.data = (JSONObject) resource.getEmbeddedData().getElement();
        }
        else {
            this.data = new JSONObject();
        }

        this.extractOptions(this.context, this.data);

        if (EdenUtils.isEmpty(this.title)) {
            if (!EdenUtils.isEmpty(title)) {
                this.title = title;
            }
            else {
                this.title = resource.getReference().getTitle();
            }
        }

        this.addComponent(PageContentComponent.class);
        if (this.data.has("components")) {
            componentHolder.addComponents(this.data.getJSONArray("components"));
        }
    }

    public String getLink() {
        return reference.toString();
    }

    public String getContent() {
        if (resource != null && !EdenUtils.isEmpty(resource.getContent())) {
            String compiledContent = resource.getContent();

            if (resource.shouldPrecompile()) {
                compiledContent = context.precompile(compiledContent, data);
            }

            return context.compile(
                    resource.getReference().getExtension(),
                    compiledContent
            );
        }
        else {
            return "";
        }
    }

    public Theme getTheme() {
        return context.getTheme();
    }

// Serialize/deserialize from JSON
//----------------------------------------------------------------------------------------------------------------------

    public JSONObject toJSON() {
        JSONObject pageJson = new JSONObject();
        pageJson.put("reference", this.reference.toJSON());
        if (this.previous != null) {
            pageJson.put("previous", this.previous.getReference().toJSON());
        }
        if (this.next != null) {
            pageJson.put("next", this.next.getReference().toJSON());
        }

        pageJson.put("description", this.description);

        JSONObject pageData = serializeData();
        if (pageData != null) {
            pageJson.put("data", pageData);
        }

        return pageJson;
    }

    protected JSONObject serializeData() {
        return this.data;
    }

    public static OrchidPage fromJSON(OrchidContext context, JSONObject source) {
        OrchidReference pageReference = OrchidReference.fromJSON(context, source.getJSONObject("reference"));
        OrchidExternalPage externalPage = new OrchidExternalPage(pageReference);

        if (source.has("previous")) {
            externalPage.setPrevious(new OrchidExternalPage(OrchidReference.fromJSON(context, source.getJSONObject("previous"))));
        }
        if (source.has("next")) {
            externalPage.setNext(new OrchidExternalPage(OrchidReference.fromJSON(context, source.getJSONObject("next"))));
        }

        externalPage.description = source.optString("description");

        if (source.has("data")) {
            externalPage.data = source.getJSONObject("className");
        }

        return externalPage;
    }

    @Override
    public String toString() {
        return this.toJSON().toString(2);
    }

// Components
//----------------------------------------------------------------------------------------------------------------------

    public void addComponent(Class<? extends OrchidComponent> componentClass) {
        componentHolder.addComponent(componentClass);
    }

    public OrchidComponent getComponent(String componentKey) {
        return componentHolder.getComponent(this, componentKey);
    }

    public Set<OrchidComponent> getComponents() {
        return componentHolder.getComponents(this);
    }

    public Set<OrchidComponent> getRemainingComponents() {
        return componentHolder.getRemainingComponents(this);
    }

// Assets
//----------------------------------------------------------------------------------------------------------------------

    @Override public void addJs(OrchidPage jsAsset) { assets.addJs(jsAsset); }
    @Override public void addCss(OrchidPage cssAsset) { assets.addCss(cssAsset); }
    @Override public List<OrchidPage> getScripts() { return context.getTheme().getScripts(); }
    @Override public List<OrchidPage> getStyles() { return context.getTheme().getStyles(); }
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
