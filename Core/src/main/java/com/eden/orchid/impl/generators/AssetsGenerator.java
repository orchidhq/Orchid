package com.eden.orchid.impl.generators;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.OrchidResource;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class AssetsGenerator extends OrchidGenerator {

    private List<OrchidPage> assets;
    private OrchidResources resources;

    @Inject
    public AssetsGenerator(OrchidResources resources) {
        this.resources = resources;
        this.priority = 1000;
    }

    @Override
    public String getName() {
        return "assets";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public JSONElement startIndexing() {
        JSONObject siteAssets = new JSONObject();

        List<OrchidResource> resourcesList = resources.getResourceEntries("assets", null, true);
        assets = new ArrayList<>();

        for (OrchidResource entry : resourcesList) {
            OrchidPage asset = new OrchidPage(entry);

            assets.add(asset);

            JSONObject index = new JSONObject();
            index.put("name", asset.getReference().getTitle());
            index.put("title", asset.getReference().getTitle());
            index.put("url", asset.getReference().toString());

            OrchidUtils.buildTaxonomy(entry, siteAssets, index);
        }

        return new JSONElement(siteAssets.optJSONObject("assets"));
    }

    @Override
    public void startGeneration() {
        for (OrchidPage asset : assets) {
            asset.renderRawContent();
        }
    }
}

