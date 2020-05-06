package com.eden.orchid.api.theme.pages;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.Collectible;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.AllOptions;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.FloatDefault;
import com.eden.orchid.api.options.annotations.ImpliedKey;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.archetypes.ConfigArchetype;
import com.eden.orchid.api.options.archetypes.SharedConfigArchetype;
import com.eden.orchid.api.render.RenderService;
import com.eden.orchid.api.render.Renderable;
import com.eden.orchid.api.resources.resource.ExternalResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.AbstractTheme;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.assets.AssetManagerDelegate;
import com.eden.orchid.api.theme.assets.CombinedAssetHolder;
import com.eden.orchid.api.theme.assets.CombinedAssetHolderKt;
import com.eden.orchid.api.theme.assets.CssPage;
import com.eden.orchid.api.theme.assets.ExtraCss;
import com.eden.orchid.api.theme.assets.ExtraJs;
import com.eden.orchid.api.theme.assets.JsPage;
import com.eden.orchid.api.theme.assets.WithAssets;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.eden.orchid.api.theme.components.MetaComponentHolder;
import com.eden.orchid.api.theme.menus.OrchidMenu;
import com.eden.orchid.impl.relations.PageRelation;
import com.eden.orchid.impl.relations.ThemeRelation;
import com.eden.orchid.utilities.OrchidExtensionsKt;
import com.eden.orchid.utilities.OrchidUtils;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.eden.orchid.utilities.OrchidExtensionsKt.from;
import static com.eden.orchid.utilities.OrchidExtensionsKt.to;

@Description(value = "A representation of a single file in your output site.", name = "Pages")
@Archetype(value = SharedConfigArchetype.class, key = "from", order = 10)
@Archetype(value = ConfigArchetype.class, key = "allPages")
public class OrchidPage implements
        OptionsHolder,
        WithAssets,
        Renderable,
        Collectible<OrchidPage>
{

    // global variables
    protected final OrchidContext context;
    protected final String templateBase = "pages";
    protected final String layoutBase = "layouts";

    protected OrchidGenerator generator;
    @AllOptions
    private Map<String, Object> allData;

    // variables that give the page identity
    protected final OrchidResource resource;
    protected OrchidReference reference;
    protected String key;
    protected Map<String, Object> data;
    protected final RenderService.RenderMode pageRenderMode;

    @Option("next") private PageRelation nextPage;
    @Option("previous") private PageRelation previousPage;
    @Option("parent") private PageRelation parentPage;

    private String compiledContent;

    @Option
    @Description("Specify a custom title for this Page, which takes precedence over the title given by its generator.")
    protected String title;

    @Option
    @Description("Specify a custom description for this page, to include in the meta description tag.")
    protected String description;

    // templates
    @Option
    @Description("The layout to embed this page in, or 'none' to render the page content without a layout. A page's " +
            "default layout, if none is specified, is `index`"
    )
    protected String layout;

    @Option
    @Description("Specify a template or a list of templates to use when rendering this page. The first template that " +
            "exists will be chosen for this page, otherwise the page's default set of templates will be searched for " +
            "(which typically is customized by the generator that produces this page)."
    )
    protected String[] template;

    // internal bookkeeping variables
    protected boolean isCurrent;
    protected boolean isIndexed;

    // SEO variables
    @Option
    @Description("Request that search engines do not index this page by adding a meta tag on in the page's head.")
    protected boolean noIndex;

    @Option
    @Description("Request that search engines do not follow links from this page by adding a meta tag on in the page's " +
            "head."
    )
    protected boolean noFollow;

    @Option
    @Description("A rough estimate of how frequently the content of this page changes, primarily to include in the " +
            "generated sitemap.xml. One of [always, hourly, daily, weekly, monthly, yearly, never]."
    )
    protected String changeFrequency;

    @Option @FloatDefault(0.5f)
    @Description("The importance of this page relative to the rest of the pages on your site. Should be a value " +
            "between 0 and 1."
    )
    protected float relativePriority;

    // variables that control page publication
    @Option @BooleanDefault(false)
    @Description("Set this page as currently being a draft. Drafts will not be included in the rendered site.")
    protected boolean draft;

    @Option
    @Description("Set when this page was published. Pages with a publish date in the future are considered a draft. " +
            "Should be a valid ISO-8601 date or datetime without timezone, such as `2018-01-01` or " +
            "`2018-01-01T08:15:30`. Note that some generators may choose to set this value based on some external " +
            "criteria, but the value in front matter should take precedence over the generator's determined publish date."
    )
    private LocalDateTime publishDate;

    @Option
    @Description("Set when this page expires. Pages with an expiry date in the past are considered a draft. Should " +
            "be a valid ISO-8601 date or datetime without timezone, such as `2018-01-01` or " +
            "`2018-01-01T08:15:30`."
    )
    private LocalDateTime expiryDate;

    @Option
    @Description("Set when this page was last modified. Should be a valid ISO-8601 date or datetime without " +
            "timezone, such as `2018-01-01` or `2018-01-01T08:15:30`."
    )
    private LocalDateTime lastModifiedDate;

    // variables that attach other objects to this page

    @Option
    @Description("The secondary only added to this page. It is common for generators to add menu items to their pages" +
            "automcatically, but the menu specified on the page will take precedence over the generator's page."
    )
    protected OrchidMenu menu;

    @Option
    @Description("The components that comprise the main content body for this page. The 'intrinsic content' of the " +
            "page, which is typically the rendered markup of the containing file, is added by default as a component " +
            "of type `pageContent` if none are specified. The full `pageContent` component is rendered within the " +
            "chosen page template. If a custom list of components is given, you will need to add the `pageContent` " +
            "component yourself."
    )
    protected ComponentHolder components;

    @Option
    @Description("The components that comprise the meta-info for this page. Typically extra scripts or meta tags " +
            "included in the `HEAD` of a page."
    )
    protected MetaComponentHolder metaComponents;

    @Option
    @Description("The default breadcrumbs to display for this page.")
    protected String defaultBreadcrumbs;

    private final Map<Integer, ThemeRelation> possibleThemes = new HashMap<>();
    private final Lazy<AbstractTheme> lazyTheme = LazyKt.lazy(() -> {
        List<Pair<Integer, ThemeRelation>> mapPairs = MapsKt.toList(possibleThemes);
        List<Pair<Integer, ThemeRelation>> filteredPairs = CollectionsKt.filter(mapPairs, it -> it.getSecond().get() != null);
        Pair<Integer, ThemeRelation> maxPriorityTheme = CollectionsKt.maxBy(filteredPairs, it -> it.getFirst());

        return maxPriorityTheme.getSecond().get();
    });

// Constructors and initialization
//----------------------------------------------------------------------------------------------------------------------

    public OrchidPage(OrchidResource resource, RenderService.RenderMode renderMode, String key, String title) {
        Intrinsics.checkNotNull(resource, "OrchidPage 'resource' cannot be null");
        Intrinsics.checkNotNull(renderMode, "OrchidPage 'renderMode' cannot be null");
        Intrinsics.checkNotNull(key, "OrchidPage 'key' cannot be null");

        this.context = resource.getReference().getContext();

        this.key = key;
        this.template = new String[]{"page"};

        this.resource = resource;
        this.reference = new OrchidReference(resource.getReference());
        this.reference.setExtension(resource.getReference().getOutputExtension());
        this.pageRenderMode = renderMode;

        initialize(title);
    }

    protected void initialize(String title) {
        this.data = OrchidExtensionsKt.extractOptionsFromResource(this, context, resource);
        postInitialize(title);
    }

    protected void postInitialize(String title) {
        if (EdenUtils.isEmpty(this.title)) {
            if (!EdenUtils.isEmpty(title)) {
                this.title = title;
            }
            else {
                this.title = to(from(resource.getReference().getTitle(), OrchidExtensionsKt::filename), OrchidExtensionsKt::titleCase);
            }
        }

        addComponents();
    }

// Get page info that has additional logic
//----------------------------------------------------------------------------------------------------------------------

    public String getLink() {
        return reference.toString(context);
    }

    public final boolean isDraft() {
        if(context.includeDrafts()) {
            return false;
        }
        else if(draft) {
            return true;
        }
        else if(publishDate != null && publishDate.isAfter(LocalDate.now().atTime(LocalTime.MAX))) {
            return true;
        }
        else if(expiryDate != null && expiryDate.isBefore(LocalDate.now().atStartOfDay())) {
            return true;
        }

        return false;
    }

    public String getContent() {
        if(context.getState().isPreBuildState()) throw new IllegalStateException("Cannot get page content until after indexing has completed");

        if(compiledContent == null) {
            compiledContent = resource.compileContent(this);
            if(compiledContent == null) {
                compiledContent = "";
            }
        }

        return compiledContent;
    }

    public void addPossibleTheme(int priority, ThemeRelation relation) {
        possibleThemes.put(priority, relation);
    }

    public @Nullable ThemeRelation getThemeRelation() {
        return null;
    }

    public AbstractTheme getTheme() {
        if(context.getState().isPreBuildState()) throw new IllegalStateException("Cannot access a page's theme until after indexing has completed");
        return lazyTheme.getValue();
    }

    public boolean shouldRender() {
        return resource.shouldRender();
    }

    public List<String> getTemplates() {
        return null;
    }

    public final List<String> getPossibleTemplates() {
        List<String> templates = new ArrayList<>();
        Collections.addAll(templates, this.template);

        List<String> declaredTemplates = getTemplates();
        if(!EdenUtils.isEmpty(declaredTemplates)) {
            templates.addAll(declaredTemplates);
        }
        templates.add(getKey());
        templates.add("page");

        return templates;
    }

    public final List<String> getPossibleLayouts() {
        List<String> layouts = new ArrayList<>();
        if(!EdenUtils.isEmpty(getLayout())) {
            layouts.add(getLayout());
        }
        if(getGenerator() != null && !EdenUtils.isEmpty(getGenerator().getLayout())) {
            layouts.add(getGenerator().getLayout());
        }
        layouts.add("index");

        return layouts;
    }

    public final OrchidResource resolveLayout() {
        return OrchidUtils.expandTemplateList(getContext(), getPossibleLayouts(), getLayoutBase())
                .map(template -> getContext().locateTemplate(template, true))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public final OrchidResource resolveTemplate() {
        return resolveTemplate(context, this);
    }

    public final String renderInLayout() {
        OrchidResource layoutResource = resolveLayout();
        if(layoutResource != null) {
            return layoutResource.compileContent(this);
        }
        return "";
    }

    public final String renderContent() {
        return renderContent(context, this);
    }

    // Serialize/deserialize from JSON
//----------------------------------------------------------------------------------------------------------------------

    public JSONObject toJSON() {
        return toJSON(false, false);
    }

    public JSONObject toJSON(boolean includePageContent, boolean includePageData) {
        JSONObject pageJson = new JSONObject();
        pageJson.put("title", this.getTitle());
        pageJson.put("reference", this.reference.toJSON(context));
        if (getPrevious() != null) {
            pageJson.put("previous", getPrevious().getReference().toJSON(context));
        }
        if (getNext() != null) {
            pageJson.put("next", getNext().getReference().toJSON(context));
        }

        pageJson.put("description", this.description);

        if (includePageContent) {
            if(resource instanceof ExternalResource) {
                if(((ExternalResource) resource).getShouldDownload()) {
                    pageJson.put("content", this.getContent());
                }
            }
            else {
                pageJson.put("content", this.getContent());
            }
        }

        if (includePageData) {
            Map<String, Object> pageData = serializeData();
            if (pageData != null) {
                pageJson.put("data", new JSONObject(pageData));
            }
        }

        return pageJson;
    }

    protected Map<String, Object> serializeData() {
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

        externalPage.title = source.optString("title");
        externalPage.description = source.optString("description");

        if (source.has("data")) {
            externalPage.data = source.getJSONObject("data").toMap();
        }

        return externalPage;
    }

    @Override
    public String toString() {
        return getLink();
    }

// Assets
//----------------------------------------------------------------------------------------------------------------------

    @Option
    @Description("Attach extra CSS assets to this page.")
    @ImpliedKey(typeKey = "asset")
    private List<ExtraCss> extraCss;

    @Option
    @Description("Attach extra Javascript assets to this page.")
    @ImpliedKey(typeKey = "asset")
    private List<ExtraJs> extraJs;

    private final Lazy<CombinedAssetHolder> pageAssets = LazyKt.lazy(
            () -> CombinedAssetHolderKt.initializePageAssets(this, getComponentHolders())
    );

    @Nonnull
    @Override
    public final AssetManagerDelegate createAssetManagerDelegate(@Nonnull OrchidContext context) { return new AssetManagerDelegate(context, this, "page", null); }

    @Nonnull
    @Override
    public final List<ExtraCss> getExtraCss() { return extraCss; }
    public final void setExtraCss(List<ExtraCss> extraCss) { this.extraCss = extraCss; }

    @Nonnull
    @Override
    public final List<ExtraJs> getExtraJs() { return extraJs; }
    public final void setExtraJs(List<ExtraJs> extraJs) { this.extraJs = extraJs; }

    /**
     * Get all CSS assets that should be included in the page `&gt;head&lt;`. Includes assets from this OrchidPage, the
     * Theme, and all registered Components.
     *
     * Assets are cached, and calling this method multiple times will not recompute or re-render assets.
     */
    public final List<CssPage> getStyles() { return pageAssets.getValue().getStyles(); }

    /**
     * Get all JS assets that should be included at the end of the page' `&gt;body&lt;`. Includes assets from this
     * OrchidPage, the Theme, and all registered Components.
     *
     * Assets are cached, and calling this method multiple times will not recompute or re-render assets.
     */
    public final List<JsPage> getScripts() { return pageAssets.getValue().getScripts(); }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadAssets(@Nonnull AssetManagerDelegate delegate) {

    }

// Callbacks
//----------------------------------------------------------------------------------------------------------------------

    protected ComponentHolder[] getComponentHolders() {
        return new ComponentHolder[] { components, metaComponents };
    }

    private void addComponents() {
        if (this.components != null && this.components.isEmpty()) {
            Map<String, Object> jsonObject = new HashMap<>();
            jsonObject.put("type", "pageContent");
            this.components.add(jsonObject);
        }
    }

    public void free() {
        resource.free();
        compiledContent = null;
    }

    @Override
    public OrchidPage getItem() {
        return this;
    }

    @Nonnull
    @Override
    public List<String> getItemIds() {
        return Collections.singletonList(title);
    }


// Page Relationships
//----------------------------------------------------------------------------------------------------------------------

    public void setNext(OrchidPage nextPage) {
        if(this.nextPage == null) this.nextPage = new PageRelation(context);
        this.nextPage.set(nextPage);
    }
    public void setNext(PageRelation nextPage) {
        this.nextPage = nextPage;
    }
    public OrchidPage getNext() {
        if (nextPage != null) {
            return nextPage.get();
        }
        return null;
    }

    public void setPrevious(OrchidPage previousPage) {
        if(this.previousPage == null) this.previousPage = new PageRelation(context);
        this.previousPage.set(previousPage);
    }
    public void setPrevious(PageRelation previousPage) {
        this.previousPage = previousPage;
    }
    public OrchidPage getPrevious() {
        if (previousPage != null) {
            return previousPage.get();
        }
        return null;
    }

    public void setParent(OrchidPage parentPage) {
        if(this.parentPage == null) this.parentPage = new PageRelation(context);
        this.parentPage.set(parentPage);
    }
    public void setParent(PageRelation parentPage) {
        this.parentPage = parentPage;
    }
    public OrchidPage getParent() {
        if (parentPage != null) {
            return parentPage.get();
        }
        return null;
    }

// Map Implementation
//----------------------------------------------------------------------------------------------------------------------

    public Map<String, Object> getMap() {
        return allData;
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

// Delombok
//----------------------------------------------------------------------------------------------------------------------

    public OrchidResource getResource() {
        return resource;
    }

    public OrchidReference getReference() {
        return reference;
    }

    public void setReference(OrchidReference reference) {
        this.reference = reference;
    }

    public OrchidContext getContext() {
        return this.context;
    }

    public String getTemplateBase() {
        return this.templateBase;
    }

    public String getLayoutBase() {
        return this.layoutBase;
    }

    public OrchidGenerator getGenerator() {
        return this.generator;
    }

    public Map<String, Object> getAllData() {
        return this.allData;
    }

    public String getKey() {
        return this.key;
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLayout() {
        return this.layout;
    }

    public boolean isCurrent() {
        return this.isCurrent;
    }

    public boolean isIndexed() {
        return this.isIndexed;
    }

    public boolean isNoIndex() {
        return this.noIndex;
    }

    public boolean isNoFollow() {
        return this.noFollow;
    }

    public String getChangeFrequency() {
        return this.changeFrequency;
    }

    public float getRelativePriority() {
        return this.relativePriority;
    }

    public LocalDateTime getPublishDate() {
        return this.publishDate;
    }

    public LocalDateTime getExpiryDate() {
        return this.expiryDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public OrchidMenu getMenu() {
        return this.menu;
    }

    public ComponentHolder getComponents() {
        return this.components;
    }

    public MetaComponentHolder getMetaComponents() {
        return metaComponents;
    }

    public String getDefaultBreadcrumbs() {
        return this.defaultBreadcrumbs;
    }

    public void setGenerator(OrchidGenerator generator) {
        this.generator = generator;
    }

    public void setAllData(Map<String, Object> allData) {
        this.allData = allData;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public void setTemplate(String[] template) {
        this.template = template;
    }

    public void setCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public void setIndexed(boolean isIndexed) {
        this.isIndexed = isIndexed;
    }

    public void setNoIndex(boolean noIndex) {
        this.noIndex = noIndex;
    }

    public void setNoFollow(boolean noFollow) {
        this.noFollow = noFollow;
    }

    public void setChangeFrequency(String changeFrequency) {
        this.changeFrequency = changeFrequency;
    }

    public void setRelativePriority(float relativePriority) {
        this.relativePriority = relativePriority;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setMenu(OrchidMenu menu) {
        this.menu = menu;
    }

    public void setComponents(ComponentHolder components) {
        this.components = components;
    }

    public void setMetaComponents(MetaComponentHolder metaComponents) {
        this.metaComponents = metaComponents;
    }

    public void setDefaultBreadcrumbs(String defaultBreadcrumbs) {
        this.defaultBreadcrumbs = defaultBreadcrumbs;
    }

    public RenderService.RenderMode getPageRenderMode() {
        return pageRenderMode;
    }
}
