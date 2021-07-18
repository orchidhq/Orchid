package com.eden.orchid.api.theme.components;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.AllOptions;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.ImpliedKey;
import com.eden.orchid.api.options.annotations.IntDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.archetypes.SharedConfigArchetype;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.render.Renderable;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.server.annotations.ImportantModularType;
import com.eden.orchid.api.theme.assets.AssetManagerDelegate;
import com.eden.orchid.api.theme.assets.ExtraCss;
import com.eden.orchid.api.theme.assets.ExtraJs;
import com.eden.orchid.api.theme.assets.WithAssets;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.eden.orchid.utilities.OrchidUtils.DEFAULT_PRIORITY;

@ImportantModularType
@Description(value = "A reusable block of content.", name = "Components")
@Archetype(value = SharedConfigArchetype.class, key = "from")
public abstract class OrchidComponent extends Prioritized implements
        OptionsHolder,
        WithAssets,
        ModularPageListItem<ComponentHolder, OrchidComponent>,
        Renderable {

    public enum MetaLocation {
        head, bodyStart, bodyEnd
    }

    protected final String templateBase = "components";
    protected final String type;
    public final boolean meta;

    protected OrchidContext context;
    protected OrchidPage page;

    @Option
    @Description("Specify a template or a list of templates to use when rendering this component. The first template " +
            "that exists will be chosen for this component."
    )
    protected String[] template;

    @Option @IntDefault(0)
    @Description("By default, components are rendered in the order in which they are declared, but the ordering can " +
            "be changed by setting the order on any individual component. A higher value for order will render that " +
            "component earlier in the list."
    )
    protected int order;

    @Option @BooleanDefault(false)
    @Description("When true, this component will not have a template rendered on the page. Useful for Components that" +
            " only add extra CSS or JS, or for temporarily removing a component from the page."
    )
    protected boolean hidden;

    @Option @BooleanDefault(false)
    @Description("When true, this component will not be wrapped in a wrapper element. The wrapper element is determined" +
            "by the theme, and it is up to the theme to ensure this is implemented properly."
    )
    protected boolean noWrapper;

    @Option
    @Nullable
    private MetaLocation metaLocation;
    private final MetaLocation defaultMetaLocation;

    @AllOptions
    private Map<String, Object> allData;

    public OrchidComponent(String type, boolean meta, @Nonnull MetaLocation defaultMetaLocation, int priority) {
        super(priority);
        this.type = type;
        this.meta = meta;
        this.defaultMetaLocation = defaultMetaLocation;
    }

    public OrchidComponent(String type, int priority) {
        this(type, false, MetaLocation.head, priority);
    }

    public OrchidComponent(String type, boolean meta) {
        this(type, meta, MetaLocation.head, DEFAULT_PRIORITY);
    }

    public OrchidComponent(String type) {
        this(type, false, MetaLocation.head, DEFAULT_PRIORITY);
    }

    @Override
    public boolean canBeUsedOnPage(
            OrchidPage containingPage,
            ComponentHolder componentHolder,
            List<Map<String, Object>> possibleComponents,
            List<OrchidComponent> currentComponents) {
        return true;
    }

    public void initialize(OrchidContext context, OrchidPage containingPage) {
        this.context = context;
        this.page = containingPage;
    }

    public Object get(String key) {
        return allData.get(key);
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
        templates.add(getType());

        return templates;
    }

    public OrchidContext getContext() {
        return this.context;
    }

    @Nonnull
    public String getTemplateBase() {
        return this.templateBase;
    }

    public String getType() {
        return this.type;
    }

    public OrchidPage getPage() {
        return this.page;
    }

    public String[] getTemplate() {
        return this.template;
    }

    public int getOrder() {
        return this.order;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public boolean isNoWrapper() {
        return this.noWrapper;
    }

    public Map<String, Object> getAllData() {
        return this.allData;
    }

    public void setPage(OrchidPage page) {
        this.page = page;
    }

    public void setTemplate(String[] template) {
        this.template = template;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setNoWrapper(boolean noWrapper) {
        this.noWrapper = noWrapper;
    }

    public void setAllData(Map<String, Object> allData) {
        this.allData = allData;
    }

    @Nullable
    public MetaLocation getMetaLocation() {
        return (metaLocation != null) ? metaLocation : defaultMetaLocation;
    }

    public void setMetaLocation(@Nullable MetaLocation metaLocation) {
        this.metaLocation = metaLocation;
    }

// Delombok
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String renderContent(OrchidContext context, OrchidPage orchidPage) {
        OrchidResource resource = resolveTemplate(context, orchidPage);
        if(resource != null) {
            return resource.compileContent(context, this);
        }
        return "";
    }

// Assets
//----------------------------------------------------------------------------------------------------------------------

    @Option
    @Description("Attach extra CSS assets to this component, to be injected into its page.")
    @ImpliedKey(typeKey = "asset")
    private List<ExtraCss> extraCss;

    @Option
    @Description("Attach extra Javascript assets to this component, to be injected into its page.")
    @ImpliedKey(typeKey = "asset")
    private List<ExtraJs> extraJs;

    @Nonnull
    @Override
    public final AssetManagerDelegate createAssetManagerDelegate(@Nonnull OrchidContext context) {
        return new AssetManagerDelegate(context, this, "component", null);
    }

    @Nonnull
    @Override
    public final List<ExtraCss> getExtraCss() {
        return extraCss;
    }
    public final void setExtraCss(List<ExtraCss> extraCss) {
        this.extraCss = extraCss;
    }

    @Nonnull
    @Override
    public final List<ExtraJs> getExtraJs() {
        return extraJs;
    }
    public final void setExtraJs(List<ExtraJs> extraJs) {
        this.extraJs = extraJs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadAssets(@Nonnull AssetManagerDelegate delegate) {

    }
}
