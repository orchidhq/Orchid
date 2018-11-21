package com.eden.orchid.api.theme.assets;

import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.resources.resource.InlineResource;
import com.eden.orchid.api.resources.resource.OrchidResource;

@Description(value = "A CSS static asset.", name = "CSS Asset")
public final class CssPage extends AssetPage {

    public CssPage(Object source, String sourceKey, OrchidResource resource, String key, String title) {
        super(source, sourceKey, resource, key, title);
    }

    public String renderAssetToPage() {
        if (resource instanceof InlineResource) {
            return "<style>\n" + resource.compileContent(this) + "\n</style>";
        }
        else {
            return "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + this.getLink() + "\"/>";
        }
    }

}
