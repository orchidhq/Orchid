package com.eden.orchid.utilities;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.impl.compilers.jtwig.TwigWalkMapFilter;
import com.eden.orchid.resources.OrchidResource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String linkTo(String link) {
        if(Orchid.query("index.internalClasses") != null) {
            String s = findInMap(link, (JSONObject) Orchid.query("index.internalClasses").getElement());
            if(!EdenUtils.isEmpty(s)) {
                return s;
            }
        }

        if(Orchid.query("index.externalClasses") != null) {
            String s = findInMap(link, (JSONObject) Orchid.query("index.externalClasses").getElement());
            if(!EdenUtils.isEmpty(s)) {
                return s;
            }
        }

        return link;
    }

    private static String findInMap(String link, JSONObject mapObject) {

        List urls = TwigWalkMapFilter.walkObject(mapObject, "url");
        String template = "<a href=\"#{$1}\">#{$2}</a>";

        for(Object object : urls) {
            if (object instanceof Map) {
                Map map = (Map) object;

                if(map.containsKey("url")) {
                    if(map.containsKey("name") && map.get("name").toString().equals(link)) {
                        return Clog.format(template, map.get("url"), map.get("name"));
                    }
                    else if(map.containsKey("qualifiedName") && map.get("qualifiedName").toString().equals(link)) {
                        return Clog.format(template, map.get("url"), map.get("name"));
                    }
                }
            }
        }

        return null;
    }

    public static List<String> wrapString(String content, int width) {
        List<String> matchList = new ArrayList<>();

        if(!EdenUtils.isEmpty(content)) {
            Pattern regex = Pattern.compile("(.{1," + width + "}(?:\\s|$))|(.{0," + width + "})", Pattern.DOTALL);
            Matcher regexMatcher = regex.matcher(content);
            while (regexMatcher.find()) {
                String line = regexMatcher.group().trim();
                if (!EdenUtils.isEmpty(line)) {
                    matchList.add(line);
                }
            }
        }

        return matchList;
    }
}
