package com.eden.orchid.api.options.archetypes;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionArchetype;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.theme.assets.AssetPage;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import java.util.Map;

@Description(value = "Allows this asset to have configurations from data files in the archetype key's directory. " +
        "This is especially useful for binary asset files which cannot have Front Matter. Additional asset configs " +
        "come from a data file at the same path as the asset itself, but in the archetype key's directory.",
        name = "Asset Config"
)
public class AssetMetadataArchetype implements OptionArchetype {

    private final OrchidContext context;

    @Inject
    public AssetMetadataArchetype(OrchidContext context) {
        this.context = context;
    }

    @Override
    public Map<String, Object> getOptions(Object target, String archetypeKey) {
        Map<String, Object> data = null;

        if(target instanceof AssetPage) {
            AssetPage page = (AssetPage) target;

            String metadataFilename = Clog.format("{}/{}/{}",
                    OrchidUtils.normalizePath(archetypeKey),
                    OrchidUtils.normalizePath(page.getResource().getReference().getOriginalPath()),
                    OrchidUtils.normalizePath(page.getResource().getReference().getOriginalFileName())
            );

            if(!EdenUtils.isEmpty(metadataFilename)) {
                data = context.getDatafile(metadataFilename);
            }
        }

        return data;
    }

}
