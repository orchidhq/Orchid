package com.eden.orchid.api.options.archetypes;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionArchetype;
import com.eden.orchid.api.theme.assets.AssetPage;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Map;

public class AssetMetadataArchetype implements OptionArchetype {

    private final OrchidContext context;

    @Inject
    public AssetMetadataArchetype(OrchidContext context) {
        this.context = context;
    }

    @Override
    public Map<String, Object> getOptions(Object target, String archetypeKey) {
        JSONObject data = null;

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

        return (data != null) ? data.toMap() : null;
    }

}
