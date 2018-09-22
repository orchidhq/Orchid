package com.eden.orchid.api.theme.assets;

import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.archetypes.AssetMetadataArchetype;
import com.eden.orchid.api.resources.resource.InlineResource;
import com.eden.orchid.api.resources.resource.OrchidResource;

@Archetype(value = AssetMetadataArchetype.class, key = "assetmeta")
@Description(value = "A Javascript static asset.", name = "JS Asset")
public final class JsPage extends AssetPage {

    public JsPage(Object source, String sourceKey, OrchidResource resource, String key, String title) {
        super(source, sourceKey, resource, key, title);
    }

    public String renderAssetToPage() {
        if(resource instanceof InlineResource) {
            return "<script>\n" + resource.compileContent(this) + "\n</script>";
        }
        else {
            return "<script src=\"" + this.getLink() + "\"></script>";
        }
    }

}
