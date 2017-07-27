package com.eden.orchid.api.indexing;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.indexing.OrchidExternalIndex;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Data;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public abstract class OrchidIndex {

    protected final String ownKey;
    protected List<OrchidPage> ownPages;
    protected Map<String, OrchidIndex> childrenPages;

    public OrchidIndex(String ownKey) {
        this.ownKey = ownKey;
        this.ownPages = new ArrayList<>();
        this.childrenPages = new HashMap<>();
    }

    public abstract Class<? extends OrchidIndex> childIndexClass();

    public void addToIndex(String taxonomy, OrchidPage page) {
        String[] pathPieces = OrchidUtils.normalizePath(taxonomy).split("/");

        if(pathPieces.length > 0) {
            addToIndex(pathPieces, page);
        }
    }

    public void addToIndex(String[] pathPieces, OrchidPage page) {
        if(pathPieces[0].length() > 0) {
            if(pathPieces.length == 1) {
                if(pathPieces[0].equals(ownKey)) {
                    this.ownPages.add(page);
                }
            }
            else {
                if(!childrenPages.containsKey(pathPieces[1])) {
                    try {
                        OrchidIndex indexInstance = this.childIndexClass().getConstructor(String.class).newInstance(pathPieces[1]);
                        childrenPages.put(pathPieces[1], indexInstance);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                childrenPages.get(pathPieces[1]).addToIndex(Arrays.copyOfRange(pathPieces, 1, pathPieces.length), page);
            }
        }
    }

    public List<OrchidPage> find(String taxonomy) {
        List<OrchidPage> foundPages = new ArrayList<>();
        String[] pathPieces = taxonomy.split("/");
        if(pathPieces.length > 0) {
            foundPages.addAll(find(pathPieces));
        }

        return foundPages;
    }

    public List<OrchidPage> find(String[] pathPieces) {
        List<OrchidPage> foundPages = new ArrayList<>();
        if(pathPieces[0].length() > 0) {
            if(pathPieces.length == 1) {
                if(pathPieces[0].equals(ownKey)) {
                    foundPages.addAll(this.getAllPages());
                }
            }
            else {
                if(childrenPages.containsKey(pathPieces[1])) {
                    foundPages.addAll(childrenPages.get(pathPieces[1]).find(Arrays.copyOfRange(pathPieces, 1, pathPieces.length)));
                }
            }
        }

        return foundPages;
    }

    public OrchidIndex findIndex(String taxonomy) {
        if(!taxonomy.contains("/")) {
            if(taxonomy.equals(ownKey)) {
                return this;
            }
            else if(childrenPages.containsKey(taxonomy)) {
                return childrenPages.get(taxonomy);
            }
        }
        else {
            String[] pathPieces = taxonomy.split("/");
            return findIndex(pathPieces);
        }

        return null;
    }

    public OrchidIndex findIndex(String[] pathPieces) {
        OrchidIndex foundIndex = this;
        if(pathPieces[0].length() > 0) {
            if(pathPieces.length == 1) {
                if(pathPieces[0].equals(ownKey)) {
                    foundIndex = this;
                }
                else if(childrenPages.containsKey(pathPieces[0])) {
                    return childrenPages.get(pathPieces[0]);
                }
            }
        }

        return foundIndex;
    }

    public List<OrchidPage> getAllPages() {
        List<OrchidPage> allPages = new ArrayList<>();
        allPages.addAll(ownPages);

        for(Map.Entry<String, OrchidIndex> entry : childrenPages.entrySet()) {
            allPages.addAll(entry.getValue().getAllPages());
        }

        return allPages;
    }

    public OrchidIndex get(String key) {
        return childrenPages.get(key);
    }

    public Map<String, OrchidIndex> getChildren() {
        return childrenPages;
    }

    private Map<String, OrchidIndex> getChildrenPages() {
        throw new NotImplementedException("");
    }

    public JSONObject toJSON() {
        JSONObject indexJson = new JSONObject();
        indexJson.put("ownKey", ownKey);
        if(ownPages.size() > 0) {
            JSONArray ownPagesJson = new JSONArray();
            for (OrchidPage page : ownPages) {
                ownPagesJson.put(page.toJSON());
            }
            indexJson.put("ownPages", ownPagesJson);
        }

        if(childrenPages.keySet().size() > 0) {
            JSONObject childrenPagesJson = new JSONObject();
            for (Map.Entry<String, OrchidIndex> childIndex : childrenPages.entrySet()) {
                childrenPagesJson.put(childIndex.getKey(), childIndex.getValue().toJSON());
            }
            indexJson.put("childrenPages", childrenPagesJson);
        }

        return indexJson;
    }

    public static OrchidIndex fromJSON(OrchidContext context, JSONObject source) {
        OrchidExternalIndex index = new OrchidExternalIndex(source.getString("ownKey"));

        if(source.has("ownPages")) {
            JSONArray ownPagesJson = source.getJSONArray("ownPages");
            List<OrchidPage> ownPages = new ArrayList<>();
            for (int i = 0; i < ownPagesJson.length(); i++) {
                OrchidPage externalPage = OrchidPage.fromJSON(context, ownPagesJson.getJSONObject(i));
                ownPages.add(externalPage);
            }
            index.ownPages = ownPages;
        }

        if(source.has("childrenPages")) {
            JSONObject childrenPagesJson = source.getJSONObject("childrenPages");
            Map<String, OrchidIndex> childrenPages = new HashMap<>();
            for (String key : childrenPagesJson.keySet()) {
                OrchidIndex childIndex = OrchidIndex.fromJSON(context, childrenPagesJson.getJSONObject(key));
                childrenPages.put(key, childIndex);
            }
            index.childrenPages = childrenPages;
        }

        return index;
    }

    @Override
    public String toString() {
        return this.toJSON().toString(2);
    }
}
