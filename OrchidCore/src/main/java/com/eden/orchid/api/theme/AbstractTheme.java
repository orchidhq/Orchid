package com.eden.orchid.api.theme;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.AllOptions;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.ImpliedKey;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource;
import com.eden.orchid.api.resources.resourcesource.ThemeResourceSource;
import com.eden.orchid.api.theme.assets.AssetManagerDelegate;
import com.eden.orchid.api.theme.assets.ExtraCss;
import com.eden.orchid.api.theme.assets.ExtraJs;
import com.eden.orchid.api.theme.assets.WithAssets;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.resources.resourcesource.PluginJarResourceSource;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractTheme implements OptionsHolder, WithAssets, Comparable<AbstractTheme> {

    @Nullable
    private OrchidResourceSource themeResourceSource;
    protected final OrchidContext context;
    protected final String key;
    protected final int priority;

    @AllOptions
    private Map<String, Object> allData;

    private boolean isUsingCurrentPage;
    private OrchidPage currentPage;

    public AbstractTheme(OrchidContext context, String key, int priority) {
        this.key = key;
        this.context = context;
        this.priority = priority;
    }

    public void clearCache() {
        themeResourceSource = null;
    }

    public void initialize() {

    }

    public String getPreferredTemplateExtension() {
        return "peb";
    }

// Resource Source
//----------------------------------------------------------------------------------------------------------------------

    @Nullable
    public OrchidResourceSource getResourceSource() {
        if(themeResourceSource == null) {
            this.themeResourceSource = PluginJarResourceSource.create(this.getClass(), priority,  ThemeResourceSource.INSTANCE);
        }

        return themeResourceSource;
    }

    protected ComponentHolder[] getComponentHolders() {
        return new ComponentHolder[] { };
    }

    public final <T> T doWithCurrentPage(@Nonnull OrchidPage currentPage, Supplier<T> callback) {
        if(currentPage == null) throw new NullPointerException("Page cannot be null");

        this.currentPage = currentPage;
        T result = callback.get();
        this.currentPage = null;
        return result;
    }

// Assets
//----------------------------------------------------------------------------------------------------------------------

    @Option
    @Description("Add extra CSS files to every page rendered with this theme, which will be compiled just like the " +
            "rest of the site's assets."
    )
    @ImpliedKey(typeKey = "asset")
    private List<ExtraCss> extraCss;

    @Option
    @Description("Add extra Javascript files to every page rendered with this theme, which will be compiled just " +
            "like the rest of the site's assets."
    )
    @ImpliedKey(typeKey = "asset")
    private List<ExtraJs> extraJs;

    @Nonnull
    @Override
    public final AssetManagerDelegate createAssetManagerDelegate(@Nonnull OrchidContext context) {
        String prefix = getKey() + "/" + Integer.toHexString(this.hashCode());
        return new AssetManagerDelegate(context, this, "theme", prefix);
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

    public Map<String, Object> getAllData() {
        return this.allData;
    }

    public void setAllData(Map<String, Object> allData) {
        this.allData = allData;
    }

    @Override
    public int compareTo(@Nonnull AbstractTheme o) {
        return this.priority - o.priority;
    }
}
