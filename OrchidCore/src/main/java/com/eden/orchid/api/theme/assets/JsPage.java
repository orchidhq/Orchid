package com.eden.orchid.api.theme.assets;

import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.resources.resource.InlineResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import lombok.Getter;
import lombok.Setter;

@Description(value = "A Javascript static asset.", name = "JS Asset")
public final class JsPage extends AssetPage {

    @Option
    @Getter
    @Setter
    private boolean async;

    @Option
    @Getter
    @Setter
    private boolean defer;

    public JsPage(Object source, String sourceKey, OrchidResource resource, String key, String title) {
        super(source, sourceKey, resource, key, title);
    }

    public String renderAssetToPage() {
        if(resource instanceof InlineResource) {
            return "<script>\n" + resource.compileContent(this) + "\n</script>";
        }
        else {
            String tagString = "<script";

            if(async) {
                tagString += " async";
            }
            if(defer) {
                tagString += " defer";
            }

            tagString += " src=\"" + this.getLink() + "\"";
            tagString += "></script>";

            return tagString;
        }
    }

}
