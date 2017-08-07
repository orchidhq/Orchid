package com.eden.orchid.api.theme.assets;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AssetHolderDelegate implements AssetHolder {

    public static final String JS_EXT = "js";
    public static final String CSS_EXT = "css";

    private List<OrchidPage> js;
    private List<OrchidPage> css;

    @Inject
    public AssetHolderDelegate() {
        this.js = new ArrayList<>();
        this.css = new ArrayList<>();
    }

    @Override
    public void addJs(OrchidPage jsAsset) {
        if(validAsset(jsAsset, JS_EXT)) {
            js.add(jsAsset);
        }
        else {
            Clog.w("#{$1}.#{$2} is not a valid JS asset, perhaps you are missing a #{$2}->#{$3} Compiler extension?",
                    jsAsset.getReference().getFileName(),
                    jsAsset.getReference().getOutputExtension(),
                    JS_EXT);
        }
    }

    @Override
    public void addCss(OrchidPage cssAsset) {
        if(validAsset(cssAsset, CSS_EXT)) {
            css.add(cssAsset);
        }
        else {
            Clog.w("#{$1}.#{$2} is not a valid CSS asset, perhaps you are missing a #{$2}->#{$3} Compiler extension?",
                    cssAsset.getReference().getFileName(),
                    cssAsset.getReference().getOutputExtension(),
                    CSS_EXT);
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

    @Override public void flushJs() {
        js.clear();
    }

    @Override public void flushCss() {
        css.clear();
    }

    private boolean validAsset(OrchidPage asset, String targetExtension) {
        return asset.getReference().getOutputExtension().equalsIgnoreCase(targetExtension);
    }
}
