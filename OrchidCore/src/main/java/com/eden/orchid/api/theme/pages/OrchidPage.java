package com.eden.orchid.api.theme.pages;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetHolderDelegate;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.menus.OrchidMenu;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OrchidPage implements OptionsHolder, AssetHolder {

    @Getter protected final OrchidContext context;

    @Getter @Setter protected OrchidGenerator generator;

    @Getter @Setter protected OrchidResource resource;
    @Getter @Setter protected OrchidReference reference;
    @Getter @Setter protected OrchidPage next;
    @Getter @Setter protected OrchidPage previous;
    @Getter @Setter protected JSONObject data;
    @Getter @Setter protected String key;

    @Getter @Setter protected boolean isCurrent;
    @Getter @Setter protected boolean isIndexed;

    @Getter @Setter @Option protected String title;
    @Getter @Setter @Option protected String description;
    @Getter @Setter @Option protected String layout;
    @Getter @Setter @Option protected String[] templates;

    @Getter
    @Setter
    @Option
    @BooleanDefault(false)
    protected boolean draft;

    @Getter @Setter @Option protected String[] extraCss;
    @Getter @Setter @Option protected String[] extraJs;

    @Getter @Setter protected AssetHolder assets;

    @Getter @Setter @Option protected OrchidMenu menu;
    @Getter @Setter @Option protected ComponentHolder components;

    public OrchidPage(OrchidResource resource, String key) {
        this(resource, key, null);
    }

    public OrchidPage(OrchidResource resource, String key, String title) {
        this(resource, key, title, null);
    }

    public OrchidPage(OrchidResource resource, String key, String title, String path) {
        this.context = resource.getContext();
        this.assets = new AssetHolderDelegate(context);

        this.key = key;
        this.templates = new String[]{"page"};

        this.resource = resource;
        this.reference = new OrchidReference(resource.getReference());
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

        addComponents();
        addAssets();
    }

    public String getLink() {
        return reference.toString();
    }

    public String getContent() {
        if (resource != null && !EdenUtils.isEmpty(resource.getContent())) {
            return resource.compileContent(data);
        }
        else {
            return "";
        }
    }

    public Theme getTheme() {
        return context.getTheme();
    }

    public boolean shouldRender() {
        return resource.shouldRender();
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

// Assets
//----------------------------------------------------------------------------------------------------------------------

    public void addComponents() {
        if (this.components.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "pageContent");
            this.components.addComponent(jsonObject);
        }
    }

    public void addAssets() {
        OrchidUtils.addExtraAssetsTo(context, extraCss, extraJs, this);
    }

    @Override
    public AssetHolder getAssetHolder() {
        return assets;
    }

    @Override
    public List<OrchidPage> getScripts() {
        List<OrchidPage> scripts = new ArrayList<>();
        scripts.addAll(context.getTheme().getScripts());
        scripts.addAll(assets.getScripts());
        addComponentAssets(scripts, OrchidComponent::getScripts);

        return scripts;
    }

    @Override
    public List<OrchidPage> getStyles() {
        List<OrchidPage> styles = new ArrayList<>();
        styles.addAll(context.getTheme().getStyles());
        styles.addAll(assets.getStyles());
        addComponentAssets(styles, OrchidComponent::getStyles);

        return styles;
    }

    private void addComponentAssets(List<OrchidPage> assets, Function<? super OrchidComponent, ? extends List<OrchidPage>> getter) {
        try {
            List<OrchidComponent> componentsList = components.getComponents();
            if (!EdenUtils.isEmpty(componentsList)) {
                componentsList
                        .stream()
                        .map(getter)
                        .forEach(assets::addAll);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
