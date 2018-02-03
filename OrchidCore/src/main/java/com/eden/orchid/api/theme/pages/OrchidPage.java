package com.eden.orchid.api.theme.pages;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.*;
import com.eden.orchid.api.options.archetypes.ConfigArchetype;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetHolderDelegate;
import com.eden.orchid.api.theme.assets.AssetPage;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.menus.OrchidMenu;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @since v1.0.0
 * @extensible classes
 */
@Archetype(value = ConfigArchetype.class, key = "allPages")
public class OrchidPage implements OptionsHolder, AssetHolder {

    // global variables
    @Getter protected final OrchidContext context;
    @Getter @Setter protected OrchidGenerator generator;
    @Getter @Setter @OptionsData private JSONElement allData;

    // variables that give the page identity and
    @Getter @Setter protected OrchidResource resource;
    @Getter @Setter protected OrchidReference reference;
    @Getter @Setter protected String key;
    @Getter @Setter @Option protected String title;
    @Getter @Setter @Option protected String description;
    @Getter @Setter protected OrchidPage next;
    @Getter @Setter protected OrchidPage previous;
    @Getter @Setter protected JSONObject data;

    // templates
    @Getter @Setter @Option protected String layout;
    @Setter @Option protected String[] templates;

    // internal bookkeeping variables
    @Getter @Setter protected boolean isCurrent;
    @Getter @Setter protected boolean isIndexed;
    private boolean hasAddedAssets;

    // SEO variables
    @Getter @Setter @Option protected boolean noIndex;
    @Getter @Setter @Option protected boolean noFollow;
    @Getter @Setter @Option protected String changeFrequency;
    @Getter @Setter @Option protected float relativePriority;

    // variables that control page publication
    @Setter @Option @BooleanDefault(false) protected boolean draft;
    @Getter @Setter @Option private LocalDate publishDate;
    @Getter @Setter @Option private LocalDate expiryDate;
    @Getter @Setter @Option private LocalDate lastModifiedDate;

    // variables that attach other objects to this page
    @Getter @Setter protected AssetHolder assets;
    @Getter @Setter @Option protected OrchidMenu menu;
    @Getter @Setter @Option protected ComponentHolder components;
    @Getter @Setter @Option protected String[] extraCss;
    @Getter @Setter @Option protected String[] extraJs;

// Constructors and initialization
//----------------------------------------------------------------------------------------------------------------------

    public OrchidPage(OrchidResource resource, String key) {
        this(resource, key, null);
    }

    public OrchidPage(OrchidResource resource, String key, String title) {
        this(resource, key, title, null);
    }

    public OrchidPage(OrchidResource resource, String key, String title, String path) {
        this.context = resource.getContext();
        this.assets = new AssetHolderDelegate(context, this, "page");
        this.components = new ComponentHolder(context, new JSONArray());

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

        initialize(title);
    }

    protected void initialize(String title) {
        this.extractOptions(this.context, this.data);
        postInitialize(title);
    }

    protected void postInitialize(String title) {
        if (EdenUtils.isEmpty(this.title)) {
            if (!EdenUtils.isEmpty(title)) {
                this.title = title;
            }
            else {
                this.title = resource.getReference().getTitle();
            }
        }

        addComponents();
    }

// Get page info that has additional logic
//----------------------------------------------------------------------------------------------------------------------

    public final String getLink() {
        return reference.toString();
    }

    public final boolean isDraft() {
        return draft || publishDate.isAfter(LocalDate.now()) || expiryDate.isBefore(LocalDate.now());
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

    public List<String> getTemplates() {
        List<String> templates = new ArrayList<>();
        Collections.addAll(templates, this.templates);

        return templates;
    }

// Serialize/deserialize from JSON
//----------------------------------------------------------------------------------------------------------------------

    public JSONObject toJSON() {
        return toJSON(false, false);
    }

    public JSONObject toJSON(boolean includePageContent, boolean includePageData) {
        JSONObject pageJson = new JSONObject();
        pageJson.put("title", this.getTitle());
        pageJson.put("reference", this.reference.toJSON());
        if (this.previous != null) {
            pageJson.put("previous", this.previous.getReference().toJSON());
        }
        if (this.next != null) {
            pageJson.put("next", this.next.getReference().toJSON());
        }

        pageJson.put("description", this.description);

        if (includePageContent) {
            pageJson.put("content", this.getContent());
        }

        if (includePageData) {
            JSONObject pageData = serializeData();
            if (pageData != null) {
                pageJson.put("data", pageData);
            }
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

// Assets and components
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public final AssetHolder getAssetHolder() {
        return assets;
    }

    @Override
    public final List<AssetPage> getScripts() {
        addAssets();
        List<AssetPage> scripts = new ArrayList<>();
        scripts.addAll(context.getTheme().getScripts());
        scripts.addAll(assets.getScripts());
        OrchidUtils.addComponentAssets(getComponentHolders(), scripts, OrchidComponent::getScripts);

        return scripts;
    }

    @Override
    public final List<AssetPage> getStyles() {
        addAssets();
        List<AssetPage> styles = new ArrayList<>();
        styles.addAll(context.getTheme().getStyles());
        styles.addAll(assets.getStyles());
        OrchidUtils.addComponentAssets(getComponentHolders(), styles, OrchidComponent::getStyles);

        return styles;
    }

    public final void addAssets() {
        if(!hasAddedAssets) {
            loadAssets();
            OrchidUtils.addExtraAssetsTo(context, extraCss, extraJs, this, this, "page");
            hasAddedAssets = true;
        }
    }

// Callbacks
//----------------------------------------------------------------------------------------------------------------------

    protected ComponentHolder[] getComponentHolders() {
        return new ComponentHolder[] { components };
    }

    public void addComponents() {
        if (this.components.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "pageContent");
            this.components.addComponent(jsonObject);
        }
    }

    public void loadAssets() {

    }

}
