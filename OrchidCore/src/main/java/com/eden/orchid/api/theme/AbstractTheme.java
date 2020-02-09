package com.eden.orchid.api.theme;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.AllOptions;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.resources.resource.JarResource;
import com.eden.orchid.api.resources.resourcesource.JarResourceSource;
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource;
import com.eden.orchid.api.resources.resourcesource.ThemeResourceSource;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetHolderDelegate;
import com.eden.orchid.api.theme.assets.CssPage;
import com.eden.orchid.api.theme.assets.JsPage;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.jar.JarFile;

public abstract class AbstractTheme implements OptionsHolder, AssetHolder, Comparable<AbstractTheme> {

    protected final OrchidContext context;
    protected final String key;
    protected final int priority;
    protected final AssetHolder assetHolder;

    @AllOptions
    private Map<String, Object> allData;

    @Option
    @Description("Add extra CSS files to every page rendered with this theme, which will be compiled just like the " +
            "rest of the site's assets."
    )
    protected String[] extraCss;

    @Option
    @Description("Add extra Javascript files to every page rendered with this theme, which will be compiled just " +
            "like the rest of the site's assets."
    )
    protected String[] extraJs;

    private boolean hasAddedAssets;

    private boolean hasRenderedAssets;

    protected String preferredTemplateExtension;

    private boolean isUsingCurrentPage;
    private OrchidPage currentPage;

    public AbstractTheme(OrchidContext context, String key, int priority) {
        this.key = key;
        this.assetHolder = new AssetHolderDelegate(context, this, "theme");
        this.preferredTemplateExtension = "peb";
        this.context = context;
        this.priority = priority;
    }

    public void clearCache() {
        assetHolder.clearAssets();
        hasAddedAssets = false;
        hasRenderedAssets = false;
    }

    public void initialize() {

    }

    public final void addAssets() {
        if(!hasAddedAssets) {
            withNamespace(getKey() + "/" + Integer.toHexString(this.hashCode()), () -> {
                loadAssets();
                OrchidUtils.addExtraAssetsTo(context, extraCss, extraJs, this, this, "theme");
            });
            hasAddedAssets = true;
        }
    }

    protected void loadAssets() {

    }

    @Nullable
    public OrchidResourceSource getResourceSource() {
        try {
            Class<?> thisClass = this.getClass();
            JarFile jarFile = JarResourceSource.Companion.jarForClass(thisClass);
            return new JarResourceSource(jarFile, priority, ThemeResourceSource.INSTANCE);
        }
        catch (Exception e) {
            return null;
        }
    }

    public final void renderAssets() {
        if (!isHasRenderedAssets()) {
            getScripts()
                    .stream()
                    .forEach(context::render);
            getStyles()
                    .stream()
                    .forEach(context::render);
            hasRenderedAssets = true;
        }
    }

    @Override
    public final List<JsPage> getScripts() {
        addAssets();
        List<JsPage> scripts = new ArrayList<>();
        scripts.addAll(assetHolder.getScripts());
        OrchidUtils.addComponentAssets(currentPage, getComponentHolders(), scripts, OrchidComponent::getScripts);

        return scripts;
    }

    @Override
    public final List<CssPage> getStyles() {
        addAssets();
        List<CssPage> styles = new ArrayList<>();
        styles.addAll(assetHolder.getStyles());
        OrchidUtils.addComponentAssets(currentPage, getComponentHolders(), styles, OrchidComponent::getStyles);

        return styles;
    }

    protected ComponentHolder[] getComponentHolders() {
        return new ComponentHolder[] { };
    }

    public final void doWithCurrentPage(OrchidPage currentPage, Consumer<AbstractTheme> callback) {
        this.currentPage = currentPage;
        callback.accept(this);
        this.currentPage = null;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractTheme)) return false;
        if (!super.equals(o)) return false;
        AbstractTheme that = (AbstractTheme) o;
        if(getAllData() != null) {
            return Objects.equals(getKey(), that.getKey()) &&
                Objects.equals(getAllData(), that.getAllData());
        }
        else {
            return Objects.equals(getKey(), that.getKey());
        }
    }

    @Override
    public int hashCode() {
        if(getAllData() != null) {
            return Objects.hash(super.hashCode(), getKey(), getAllData());
        }
        else {
            return Objects.hash(super.hashCode(), getKey());
        }
    }

    public OrchidContext getContext() {
        return this.context;
    }

    public String getKey() {
        return this.key;
    }

    public AssetHolder getAssetHolder() {
        return this.assetHolder;
    }

    public Map<String, Object> getAllData() {
        return this.allData;
    }

    public String[] getExtraCss() {
        return this.extraCss;
    }

    public String[] getExtraJs() {
        return this.extraJs;
    }

    public boolean isHasRenderedAssets() {
        return this.hasRenderedAssets;
    }

    public String getPreferredTemplateExtension() {
        return this.preferredTemplateExtension;
    }

    public void setAllData(Map<String, Object> allData) {
        this.allData = allData;
    }

    public void setExtraCss(String[] extraCss) {
        this.extraCss = extraCss;
    }

    public void setExtraJs(String[] extraJs) {
        this.extraJs = extraJs;
    }

    public void setHasRenderedAssets(boolean hasRenderedAssets) {
        this.hasRenderedAssets = hasRenderedAssets;
    }

    public void setPreferredTemplateExtension(String preferredTemplateExtension) {
        this.preferredTemplateExtension = preferredTemplateExtension;
    }

    @Override
    public int compareTo(@NotNull AbstractTheme o) {
        return this.priority - o.priority;
    }
}
