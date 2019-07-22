package com.eden.orchid.api.theme.assets;

import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.resources.resource.InlineResource;
import com.eden.orchid.api.resources.resource.OrchidResource;

@Description(value = "A Javascript static asset.", name = "JS Asset")
public final class JsPage extends AssetPage {
    @Option
    private boolean async;
    @Option
    private boolean defer;
    @Option
    private boolean module;
    @Option
    private boolean nomodule;

    public JsPage(Object source, String sourceKey, OrchidResource resource, String key, String title) {
        super(source, sourceKey, resource, key, title);
    }

    public String renderAssetToPage() {
        if (resource instanceof InlineResource) {
            return applyStartTag()
                    + applyModuleNoModule()
                    + applyInlineScript()
                    + applyCloseTag();
        } else {
            return applyStartTag()
                    + applyAsyncDefer()
                    + applyModuleNoModule()
                    + applyScriptSource()
                    + applyCloseTag();
        }
    }

    public boolean isAsync() {
        return this.async;
    }

    public void setAsync(final boolean async) {
        this.async = async;
    }

    public boolean isDefer() {
        return this.defer;
    }

    public void setDefer(final boolean defer) {
        this.defer = defer;
    }

    public boolean isModule() {
        return this.module;
    }

    public void setModule(final boolean module) {
        this.module = module;
    }

    public boolean isNomodule() {
        return nomodule;
    }

    public JsPage setNomodule(boolean nomodule) {
        this.nomodule = nomodule;
        return this;
    }

    private String applyAsyncDefer() {
        String tagString = "";
        if (async) {
            tagString += " async";
        }
        if (defer) {
            tagString += " defer";
        }

        return tagString;
    }

    private String applyModuleNoModule() {
        String tagString = "";
        // if tagged as module, cannot be a `nomodule`
        if (module) {
            tagString += " type=\"module\"";
        } else if (nomodule) {
            tagString += " nomodule";
        }
        return tagString;
    }

    private String applyScriptSource() {
        return " src=\"" + this.getLink() + "\">";
    }

    private String applyInlineScript() {
        return ">\n" + resource.compileContent(this) + "\n";
    }

    private String applyStartTag() {
        return "<script";
    }

    private String applyCloseTag() {
        return "</script>";
    }
}
