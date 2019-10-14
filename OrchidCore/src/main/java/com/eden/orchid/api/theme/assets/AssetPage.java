package com.eden.orchid.api.theme.assets;

import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.archetypes.AssetMetadataArchetype;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import kotlin.collections.CollectionsKt;

import java.util.HashMap;
import java.util.Map;

@Archetype(value = AssetMetadataArchetype.class, key = "assetmeta")
@Description(value = "A static asset, like Javascript, CSS, or an image.", name = "Asset")
public class AssetPage extends OrchidPage {

    private Object source;
    private String sourceKey;
    private boolean rendered;

    @Option
    @Description("The asset alt text.")
    private String alt;

    @Option
    @Description("Arbitrary attributes to apply to this element when rendered to page")
    private Map<String, String> attrs = new HashMap<>();

    public AssetPage(Object source, String sourceKey, OrchidResource resource, String key, String title) {
        super(resource, key, title);
        this.source = source;
        this.sourceKey = sourceKey;
        reference.setUsePrettyUrl(false);
    }

    public String renderAssetToPage() {
        return "";
    }

    protected String renderAttrs() {
        return CollectionsKt.joinToString(attrs.entrySet(), " ", "", "", -1, "...", entry -> "" + entry.getKey() + "=\"" + entry.getValue() + "\"");
    }

    @Override
    public String getLink() {
        if(!rendered) {
            AssetPage assetPage = context.getAssetManager().getActualAsset(this);
            if(assetPage == null) {
                context.getAssetManager().addAsset(this, true);
            }
        }

        return super.getLink();
    }

// Delombok
//----------------------------------------------------------------------------------------------------------------------

    public Object getSource() {
        return this.source;
    }

    public String getSourceKey() {
        return this.sourceKey;
    }

    public boolean isRendered() {
        return this.rendered;
    }

    public String getAlt() {
        return this.alt;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }
}
