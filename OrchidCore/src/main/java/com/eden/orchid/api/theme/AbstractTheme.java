package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetPage;
import com.eden.orchid.api.theme.assets.ThemeAssetHolder;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTheme extends PluginResourceSource implements OptionsHolder, AssetHolder {

    @Getter protected final String key;
    @Getter protected final AssetHolder assetHolder;

    @Getter @Setter @Option protected String[] extraCss;
    @Getter @Setter @Option protected String[] extraJs;

    private boolean hasAddedAssets;
    @Getter @Setter private boolean hasRenderedAssets;

    @Getter @Setter protected String preferredTemplateExtension;

    public AbstractTheme(OrchidContext context, String key, int priority) {
        super(context, priority);
        this.key = key;
        this.assetHolder = new ThemeAssetHolder(context, this);
        this.preferredTemplateExtension = "peb";
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
            loadAssets();
            OrchidUtils.addExtraAssetsTo(context, extraCss, extraJs, this, this, "theme");
            hasAddedAssets = true;
        }
    }

    protected void loadAssets() {

    }

    public final void renderAssets() {
        if (!isHasRenderedAssets()) {
            getScripts()
                    .stream()
                    .forEach(context::renderRaw);
            getStyles()
                    .stream()
                    .forEach(context::renderRaw);
            hasRenderedAssets = true;
        }
    }

    @Override
    public final List<AssetPage> getScripts() {
        addAssets();
        List<AssetPage> scripts = new ArrayList<>();
        scripts.addAll(assetHolder.getScripts());
        OrchidUtils.addComponentAssets(getComponentHolders(), scripts, OrchidComponent::getScripts);

        return scripts;
    }

    @Override
    public final List<AssetPage> getStyles() {
        addAssets();
        List<AssetPage> styles = new ArrayList<>();
        styles.addAll(assetHolder.getStyles());
        OrchidUtils.addComponentAssets(getComponentHolders(), styles, OrchidComponent::getStyles);

        return styles;
    }

    protected ComponentHolder[] getComponentHolders() {
        return new ComponentHolder[] { };
    }

}
