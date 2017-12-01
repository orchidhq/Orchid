package com.eden.orchid.api.theme.assets;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.ExternalResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FilenameUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public final class AssetHolderDelegate implements AssetHolder {

    protected final OrchidContext context;

    public static final String JS_EXT = "js";
    public static final String CSS_EXT = "css";

    private final List<OrchidPage> js;
    private final List<OrchidPage> css;

    @Getter @Setter
    private boolean shouldDownloadAssets;

    @Inject
    public AssetHolderDelegate(OrchidContext context) {
        this.context = context;
        this.js = new ArrayList<>();
        this.css = new ArrayList<>();
    }

    @Override
    public void addAssets() {
        throw new UnsupportedOperationException("AssetHolderDelegate cannot add its own assets");
    }

    @Override
    public AssetHolder getAssetHolder() {
        return this;
    }

    @Override
    public void addJs(OrchidPage jsAsset) {
        if(validAsset(jsAsset, JS_EXT)) {
            jsAsset.getReference().setUsePrettyUrl(false);
            js.add(jsAsset);
            context.getGlobalAssetHolder().addJs(jsAsset);
        }
        else {
            Clog.w("#{$1}.#{$2} is not a valid JS asset, perhaps you are missing a #{$2}->#{$3} Compiler extension?",
                    jsAsset.getReference().getFileName(),
                    jsAsset.getReference().getOutputExtension(),
                    JS_EXT);
        }
    }

    @Override
    public void addJs(String jsAsset) {
        OrchidResource resource = context.getResourceEntry(jsAsset);
        if(resource != null) {
            if(resource instanceof ExternalResource && shouldDownloadExternalAssets()) {
                ((ExternalResource) resource).setDownload(true);
            }
            addJs(new OrchidPage(resource, FilenameUtils.getBaseName(jsAsset)));
        }
        else {
            Clog.w("Could not find JS asset: {}", jsAsset);
        }
    }

    @Override
    public void addCss(OrchidPage cssAsset) {
        if(validAsset(cssAsset, CSS_EXT)) {
            cssAsset.getReference().setUsePrettyUrl(false);
            css.add(cssAsset);
            context.getGlobalAssetHolder().addCss(cssAsset);
        }
        else {
            Clog.w("#{$1}.#{$2} is not a valid CSS asset, perhaps you are missing a #{$2}->#{$3} Compiler extension?",
                    cssAsset.getReference().getFileName(),
                    cssAsset.getReference().getOutputExtension(),
                    CSS_EXT);
        }
    }

    @Override
    public void addCss(String cssAsset) {
        OrchidResource resource = context.getResourceEntry(cssAsset);
        if(resource != null) {
            if(resource instanceof ExternalResource && shouldDownloadExternalAssets()) {
                ((ExternalResource) resource).setDownload(true);
            }
            addCss(new OrchidPage(resource, FilenameUtils.getBaseName(cssAsset)));
        }
        else {
            Clog.w("Could not find CSS asset: {}", cssAsset);
        }
    }

    @Override
    public List<OrchidPage> getScripts() {
        return js;
    }

    @Override
    public List<OrchidPage> getStyles() {
        return css;
    }

    @Override
    public void flushJs() {
        js.clear();
    }

    @Override
    public void flushCss() {
        css.clear();
    }

    @Override
    public void clearAssets() {
        flushJs();
        flushCss();
    }


    private boolean validAsset(OrchidPage asset, String targetExtension) {
        return asset.getReference().getOutputExtension().equalsIgnoreCase(targetExtension);
    }

    @Override
    public boolean shouldDownloadExternalAssets() {
        return shouldDownloadAssets;
    }
}
