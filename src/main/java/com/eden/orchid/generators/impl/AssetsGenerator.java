package com.eden.orchid.generators.impl;

import com.eden.common.json.JSONElement;
import com.eden.orchid.generators.Generator;
import com.eden.orchid.resources.OrchidPage;
import com.eden.orchid.resources.OrchidResource;
import com.eden.orchid.resources.OrchidResources;
import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@AutoRegister
public class AssetsGenerator implements Generator {

    private List<OrchidPage> assets;

    @Override
    public int priority() {
        return 100;
    }

    @Override
    public String getName() {
        return "assets";
    }

    @Override
    public JSONElement startIndexing() {
        JSONObject siteAssets = new JSONObject();

        List<OrchidResource> resources = OrchidResources.getResourceEntries("assets", null, true);
        assets = new ArrayList<>();

        for (OrchidResource entry : resources) {
            OrchidPage asset = new OrchidPage(entry);

            assets.add(asset);

            JSONObject index = new JSONObject();
            index.put("name", asset.getReference().getTitle());
            index.put("title", asset.getReference().getTitle());
            index.put("url", asset.getReference().toString());

            OrchidUtils.buildTaxonomy(entry, siteAssets, index);
        }

        return new JSONElement(siteAssets.getJSONObject("assets"));
    }

    @Override
    public void startGeneration() {
        for (OrchidPage asset : assets) {
            asset.renderRawContent();
        }
    }
}

