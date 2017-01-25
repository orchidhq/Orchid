package com.eden.orchid.generators.impl;

import com.eden.common.json.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.generators.Generator;
import com.eden.orchid.resources.OrchidResource;
import com.eden.orchid.resources.OrchidResources;
import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

@AutoRegister
public class AssetsGenerator implements Generator {

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

        List<OrchidResource> assets = OrchidResources.getResourceEntries("assets", null, true);
        for(OrchidResource asset : assets) {
            JSONObject file = new JSONObject();

            String filePath = asset.getReference().getFullPath();
            String fileName = asset.getReference().getFileName() + "." + asset.getReference().getOutputExtension();

            file.put("url", OrchidUtils.applyBaseUrl(filePath + File.separator + fileName));

            buildTaxonomy(asset, siteAssets, file);
        }

//        return new JSONElement(siteAssets.getJSONObject("assets"));
        return new JSONElement(siteAssets);
    }

    @Override
    public void startGeneration() {
        List<OrchidResource> assets = OrchidResources.getResourceEntries("assets", null, true);

        for(OrchidResource asset : assets) {
            JSONObject data = new JSONObject(Orchid.getRoot().toMap());
            data.put("file", asset.getEmbeddedData().getElement());

            String output = (!OrchidUtils.isEmpty(asset.getContent()))
                    ? Orchid.getTheme().compile(asset.getReference().getExtension(), asset.getContent(), data)
                    : "";

            String filePath = asset.getReference().getFullPath();
            String fileName = asset.getReference().getFileName() + "." + asset.getReference().getOutputExtension();

            OrchidResources.writeFile(filePath, fileName, output);
        }
    }

    public static void buildTaxonomy(OrchidResource asset, JSONObject siteAssets, JSONObject file) {
        buildTaxonomy(asset.getReference().getFullPath(), siteAssets, file);
    }

    public static void buildTaxonomy(String taxonomy, JSONObject siteAssets, JSONObject file) {
        if(!OrchidUtils.isEmpty(taxonomy)) {
            taxonomy = taxonomy + File.separator + "files";
        }
        else {
            taxonomy = "files";
        }

        String[] pathPieces = taxonomy.split(File.separator);

        JSONObject root = siteAssets;
        for(int i = 0; i < pathPieces.length; i++) {
            if(root.has(pathPieces[i]) && root.get(pathPieces[i]) instanceof JSONArray) {
                root.getJSONArray(pathPieces[i]).put(file);
            }
            else {
                if(root.has(pathPieces[i]) && root.get(pathPieces[i]) instanceof JSONObject) {
                    root = root.getJSONObject(pathPieces[i]);
                }
                else {
                    if(i == pathPieces.length - 1) {
                        root.put(pathPieces[i], new JSONArray());
                        root.getJSONArray(pathPieces[i]).put(file);
                    }
                    else {
                        root.put(pathPieces[i], new JSONObject());
                        root = root.getJSONObject(pathPieces[i]);
                    }
                }
            }
        }
    }
}

