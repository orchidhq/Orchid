package com.eden.orchid.api.theme.assets;

import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.archetypes.AssetMetadataArchetype;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

@Archetype(value = AssetMetadataArchetype.class, key = "assetmeta")
@Description(value = "A static asset, like Javascript, CSS, or an image.", name = "Asset")
public class AssetPage extends OrchidPage {

    @Getter @Setter
    private Object source;

    @Getter @Setter
    private String sourceKey;

    @Getter @Setter
    @Option
    @Description("The asset alt text.")
    private String alt;

    public AssetPage(Object source, String sourceKey, OrchidResource resource, String key, String title) {
        super(resource, key, title);
        this.source = source;
        this.sourceKey = sourceKey;
    }

    public String renderAssetToPage() {
        return "";
    }

}
