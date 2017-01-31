package com.eden.orchid.utilities;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.resources.OrchidResource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public final class OrchidUtils {

    public static String applyBaseUrl(String url) {
        String baseUrl = "";

        if (Orchid.query("options.baseUrl") != null) {
            baseUrl = Orchid.query("options.baseUrl").toString();
        }

        return baseUrl + File.separator + url;
    }

    public static void buildTaxonomy(OrchidResource asset, JSONObject siteAssets, JSONObject file) {
        buildTaxonomy(asset.getReference().getFullPath(), siteAssets, file);
    }

    public static void buildTaxonomy(String taxonomy, JSONObject siteAssets, JSONObject file) {
        if (!EdenUtils.isEmpty(taxonomy)) {
            taxonomy = taxonomy + File.separator + "files";
        }
        else {
            taxonomy = "files";
        }

        String[] pathPieces = taxonomy.split(File.separator);

        JSONObject root = siteAssets;
        for (int i = 0; i < pathPieces.length; i++) {
            if (root.has(pathPieces[i]) && root.get(pathPieces[i]) instanceof JSONArray) {
                root.getJSONArray(pathPieces[i]).put(file);
            }
            else {
                if (root.has(pathPieces[i]) && root.get(pathPieces[i]) instanceof JSONObject) {
                    root = root.getJSONObject(pathPieces[i]);
                }
                else {
                    if (i == pathPieces.length - 1) {
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
