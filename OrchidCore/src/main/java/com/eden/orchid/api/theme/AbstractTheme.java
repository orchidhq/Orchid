package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.ThemeAssetHolder;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractTheme extends PluginResourceSource implements OptionsHolder, AssetHolder {

    @Getter protected final String key;
    @Getter protected final AssetHolder assetHolder;

    @Getter @Setter @Option protected String[] extraCss;
    @Getter @Setter @Option protected String[] extraJs;

    @Getter @Setter protected boolean hasRenderedAssets;

    public AbstractTheme(OrchidContext context, String key, int priority) {
        super(context, priority);
        this.key = key;
        this.assetHolder = new ThemeAssetHolder(context, this);
    }

    public void clearCache() {
        assetHolder.clearAssets();
        hasRenderedAssets = false;
    }

    public void initialize() {

    }

    public void addAssets() {
        OrchidUtils.addExtraAssetsTo(context, extraCss, extraJs, this, this, "theme");
    }

    public void renderAssets() {
        if (!hasRenderedAssets) {
            getScripts()
                    .stream()
                    .forEach(context::renderRaw);
            getStyles()
                    .stream()
                    .forEach(context::renderRaw);
            hasRenderedAssets = true;
        }
    }

}
