package com.eden.orchid.api.theme.pages;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.AllOptions;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.archetypes.ConfigArchetype;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetHolderDelegate;
import com.eden.orchid.api.theme.assets.AssetPage;
import com.eden.orchid.api.theme.breadcrumbs.Breadcrumb;
import com.eden.orchid.api.theme.breadcrumbs.BreadcrumbHolder;
import com.eden.orchid.api.theme.breadcrumbs.BreadcrumbHolderDelegate;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.menus.OrchidMenu;
import com.eden.orchid.utilities.OrchidExtensionsKt;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.eden.orchid.utilities.OrchidExtensionsKt.from;
import static com.eden.orchid.utilities.OrchidExtensionsKt.to;

/**
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
@Archetype(value = ConfigArchetype.class, key = "allPages")
public class OrchidPage implements OptionsHolder, AssetHolder {

    // global variables
    @Getter protected final OrchidContext context;
    @Getter @Setter protected OrchidGenerator generator;
    @Getter @Setter @AllOptions
    private Map<String, Object> allData;
    @Getter @Setter private Map<String, Object> _map;

    // variables that give the page identity
    @Getter @Setter protected OrchidResource resource;
    @Getter @Setter protected OrchidReference reference;
    @Getter @Setter protected String key;
    @Getter @Setter protected OrchidPage next;
    @Getter @Setter protected OrchidPage previous;
    @Getter @Setter protected OrchidPage parent;
    @Getter @Setter protected JSONObject data;

    @Getter @Setter
    @Option
    @Description("Specify a custom title for this Page, which takes precedence over the title given by its generator.")
    protected String title;

    @Getter @Setter
    @Option
    @Description("Specify a custom description for this page, to include in the meta description tag.")
    protected String description;

    // templates
    @Getter @Setter
    @Option
    @Description("The layout to embed this page in, or 'none' to render the page content without a layout. A page's " +
            "default layout, if none is specified, is `index`"
    )
    protected String layout;

    @Setter
    @Option
    @Description("Specify a template or a list of templates to use when rendering this page. The first template that " +
            "exists will be chosen for this page, otherwise the page's default set of templates will be searched for " +
            "(which typically is customized by the generator that produces this page)."
    )
    protected String[] template;

    // internal bookkeeping variables
    @Getter @Setter protected boolean isCurrent;
    @Getter @Setter protected boolean isIndexed;
    private boolean hasAddedAssets;

    // SEO variables
    @Getter @Setter
    @Option
    @Description("Request that search engines do not index this page by adding a meta tag on in the page's head.")
    protected boolean noIndex;

    @Getter @Setter
    @Option
    @Description("Request that search engines do not follow links from this page by adding a meta tag on in the page's " +
            "head."
    )
    protected boolean noFollow;

    @Getter @Setter
    @Option
    @Description("A rough estimate of how frequently the content of this page changes, primarily to include in the " +
            "generated sitemap.xml. One of [always, hourly, daily, weekly, monthly, yearly, never]."
    )
    protected String changeFrequency;

    @Getter @Setter
    @Option
    @Description("The importance of this page relative to the rest of the pages on your site. Should be a value " +
            "between 0 and 1."
    )
    protected float relativePriority;

    // variables that control page publication
    @Setter
    @Option @BooleanDefault(false)
    @Description("Set this page as currently being a draft. Drafts will not be included in the rendered site.")
    protected boolean draft;

    @Getter @Setter
    @Option
    @Description("Set when this page was published. Pages with a publish date in the future are considered a draft. " +
            "Should be a valid ISO-8601 date or datetime without timezone, such as `2018-01-01` or " +
            "`2018-01-01T08:15:30`. Note that some generators may choose to set this value based on some external " +
            "criteria, but the value in front matter should take precedence over the generator's determined publish date."
    )
    private LocalDateTime publishDate;

    @Getter @Setter
    @Option
    @Description("Set when this page expires. Pages with an expiry date in the past are considered a draft. Should " +
            "be a valid ISO-8601 date or datetime without timezone, such as `2018-01-01` or " +
            "`2018-01-01T08:15:30`."
    )
    private LocalDateTime expiryDate;

    @Getter @Setter
    @Option
    @Description("Set when this page was last modified. Should be a valid ISO-8601 date or datetime without " +
            "timezone, such as `2018-01-01` or `2018-01-01T08:15:30`."
    )
    private LocalDateTime lastModifiedDate;

    // variables that attach other objects to this page
    @Getter @Setter protected AssetHolder assets;
    @Setter protected BreadcrumbHolder breadcrumbs;

    @Getter @Setter
    @Option
    @Description("The secondary only added to this page. It is common for generators to add menu items to their pages" +
            "automcatically, but the menu specified on the page will take precedence over the generator's page."
    )
    protected OrchidMenu menu;

    @Getter @Setter
    @Option
    @Description("The components that comprise the main content body for this page. The 'intrinsic content' of the " +
            "page, which is typically the rendered markup of the containing file, is added by default as a component " +
            "of type `pageContent` if none are specified. The full `pageContent` component is rendered within the " +
            "chosen page template. If a custom list of components is given, you will need to add the `pageContent` " +
            "component yourself."
    )
    protected ComponentHolder components;

    @Getter @Setter
    @Option
    @Description("Add extra CSS files to this page only, which will be compiled just like the rest of the site's " +
            "assets."
    )
    protected String[] extraCss;

    @Getter @Setter
    @Option
    @Description("Add extra Javascript files to every this page only, which will be compiled just like the rest of " +
            "the site's assets."
    )
    protected String[] extraJs;

    @Getter @Setter
    @Option
    @Description("The default breadcrumbs to display for this page.")
    protected String defaultBreadcrumbs;

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
        this.breadcrumbs = new BreadcrumbHolderDelegate(context);

        this.key = key;
        this.template = new String[]{"page"};

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
                this.title = to(from(resource.getReference().getTitle(), OrchidExtensionsKt::camelCase), OrchidExtensionsKt::titleCase);
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
        return draft || publishDate.isAfter(LocalDate.now().atTime(LocalTime.MAX)) || expiryDate.isBefore(LocalDate.now().atStartOfDay());
    }

    public String getContent() {
        if (resource != null && !EdenUtils.isEmpty(resource.getContent())) {
            return resource.compileContent(this);
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
        Collections.addAll(templates, this.template);

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

// Assets, Components, Breadcrumbs
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public final AssetHolder getAssetHolder() {
        return assets;
    }

    @Override
    public final List<AssetPage> getScripts() {
        addAssets();
        List<AssetPage> scripts = new ArrayList<>();
        context.getTheme().doWithCurrentPage(this, (theme) -> scripts.addAll(context.getTheme().getScripts()));
        scripts.addAll(assets.getScripts());
        OrchidUtils.addComponentAssets(this, getComponentHolders(), scripts, OrchidComponent::getScripts);

        return scripts;
    }

    @Override
    public final List<AssetPage> getStyles() {
        addAssets();
        List<AssetPage> styles = new ArrayList<>();
        context.getTheme().doWithCurrentPage(this, (theme) -> styles.addAll(context.getTheme().getStyles()));
        styles.addAll(assets.getStyles());
        OrchidUtils.addComponentAssets(this, getComponentHolders(), styles, OrchidComponent::getStyles);

        return styles;
    }

    public final void addAssets() {
        if(!hasAddedAssets) {
            loadAssets();
            OrchidUtils.addExtraAssetsTo(context, extraCss, extraJs, this, this, "page");
            hasAddedAssets = true;
        }
    }

    public final BreadcrumbHolder getBreadcrumbHolder() {
        return breadcrumbs;
    }

    public final List<Breadcrumb> getBreadCrumbs() {
        breadcrumbs.setDefaultBreadcrumbs(defaultBreadcrumbs);
        return breadcrumbs.getBreadCrumbs().get(this);
    }

    public final List<Breadcrumb> getBreadCrumbs(String key) {
        return breadcrumbs.getBreadCrumbs(key).get(this);
    }

// Callbacks
//----------------------------------------------------------------------------------------------------------------------

    protected ComponentHolder[] getComponentHolders() {
        return new ComponentHolder[] { components };
    }

    public void addComponents() {
        if (this.components != null && this.components.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "pageContent");
            this.components.add(jsonObject);
        }
    }

    public void loadAssets() {

    }

// Map Implementation
//----------------------------------------------------------------------------------------------------------------------

    public Map<String, Object> getMap() {
        if(_map == null) {
            _map = allData;
        }

        return _map;
    }

    public boolean has(String key) {
        return getMap().containsKey(key);
    }

    public Object get(String key) {
        // TODO: make this method also return values by reflection, so that anything that needs to dynamically get a property by name can get it from this one method
        return getMap().get(key);
    }

    public Object query(String key) {
        JSONElement result = new JSONElement(new JSONObject(getMap())).query(key);
        return (result != null) ? result.getElement() : null;
    }

}
