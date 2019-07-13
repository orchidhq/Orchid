package com.eden.orchid.api.theme.components;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.AllOptions;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.IntDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.archetypes.SharedConfigArchetype;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.render.Renderable;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.server.annotations.Extensible;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetHolderDelegate;
import com.eden.orchid.api.theme.assets.CssPage;
import com.eden.orchid.api.theme.assets.JsPage;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
@Extensible
@Description(value = "A reusable block of content.", name = "Components")
@Archetype(value = SharedConfigArchetype.class, key = "from")
public abstract class OrchidComponent extends Prioritized implements
        OptionsHolder,
        AssetHolder,
        ModularPageListItem<ComponentHolder, OrchidComponent>,
        Renderable {

    protected final String templateBase = "components";
    protected final String type;
    protected AssetHolder assetHolder;
    private boolean hasAddedAssets;

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

    @Option
    @Description("Add extra CSS files to the page containing this Component, which will be compiled just like the " +
            "rest of the site's assets."
    )
    protected String[] extraCss;

    @Option
    @Description("Add extra Javascript files to the page containing this Component, which will be compiled just like " +
            "the rest of the site's assets."
    )
    protected String[] extraJs;

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

    @AllOptions
    private Map<String, Object> allData;

    @Inject
    public OrchidComponent(String type, int priority) {
        super(priority);
        this.type = type;
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
        this.assetHolder = new AssetHolderDelegate(context, this, "component");
    }

    @Override
    public final void addAssets() {
        if(!hasAddedAssets) {
            loadAssets();
            OrchidUtils.addExtraAssetsTo(context, extraCss, extraJs, this, this, "component");
            hasAddedAssets = true;
        }
    }

    @Override
    public final List<JsPage> getScripts() {
        addAssets();
        return assetHolder.getScripts();
    }

    @Override
    public final List<CssPage> getStyles() {
        addAssets();
        return assetHolder.getStyles();
    }

    protected void loadAssets() {

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

    public String getTemplateBase() {
        return this.templateBase;
    }

    public String getType() {
        return this.type;
    }

    public AssetHolder getAssetHolder() {
        return this.assetHolder;
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

    public String[] getExtraCss() {
        return this.extraCss;
    }

    public String[] getExtraJs() {
        return this.extraJs;
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

    public void setExtraCss(String[] extraCss) {
        this.extraCss = extraCss;
    }

    public void setExtraJs(String[] extraJs) {
        this.extraJs = extraJs;
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

// Delombok
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String renderContent(OrchidContext context, OrchidPage orchidPage) {
        OrchidResource resource = resolveTemplate(context, orchidPage);
        if(resource != null) {
            return resource.compileContent(this);
        }
        return "";
    }
}
