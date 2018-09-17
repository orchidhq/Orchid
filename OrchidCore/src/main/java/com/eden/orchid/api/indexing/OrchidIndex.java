package com.eden.orchid.api.indexing;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrchidIndex {

    protected final OrchidIndex parent;

    protected final String ownKey;
    protected List<OrchidPage> ownPages;
    protected Map<String, OrchidIndex> childrenPages;

    public OrchidIndex(OrchidIndex parent, String ownKey) {
        this.ownKey = ownKey;
        this.parent = parent;
        this.ownPages = new ArrayList<>();
        this.childrenPages = new HashMap<>();
    }

    public void addToIndex(String taxonomy, OrchidPage page) {
        String[] pathPieces = OrchidUtils.normalizePath(taxonomy).split("/");

        if (pathPieces.length > 0) {
            addToIndex(pathPieces, page);
        }
    }

    public void addToIndex(String[] pathPieces, OrchidPage page) {
        // we have a path to add to this index
        if (!EdenUtils.isEmpty(pathPieces) && !EdenUtils.isEmpty(pathPieces[0])) {

            // we are at the correct index, because our own key is the current first level of `pathPieces`
            if (pathPieces[0].equals(ownKey)) {
                // this is the final piece of the path, add it here
                if (pathPieces.length == 1) {
                    this.ownPages.add(page);
                }

                // there are inner levels of the path, recurse and add it there
                else {
                    String nextPathPiece = pathPieces[1];

                    // we haven't created an index for the next path piece, create one by reflection
                    if (!childrenPages.containsKey(nextPathPiece)) {
                        try {
                            OrchidIndex indexInstance = new OrchidIndex(this, nextPathPiece);
                            childrenPages.put(nextPathPiece, indexInstance);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // get the child index at the next path level and add the page there
                    childrenPages.get(nextPathPiece).addToIndex(Arrays.copyOfRange(pathPieces, 1, pathPieces.length), page);
                }
            }
        }
    }

    public List<OrchidPage> find(String taxonomy) {
        return find(taxonomy.split("/"));
    }

    public List<OrchidPage> find(String[] pathPieces) {
        List<OrchidPage> foundPages = new ArrayList<>();
        // we have a path to search
        if (!EdenUtils.isEmpty(pathPieces) && !EdenUtils.isEmpty(pathPieces[0])) {
            if (pathPieces.length == 1 && pathPieces[0].equals(ownKey)) {
                foundPages.addAll(this.getAllPages());
            }
            else {
                if (childrenPages.containsKey(pathPieces[1])) {
                    foundPages.addAll(childrenPages.get(pathPieces[1]).find(Arrays.copyOfRange(pathPieces, 1, pathPieces.length)));
                }
            }
        }

        return foundPages;
    }

    public OrchidPage findPage(String taxonomy) {
        return findPage(taxonomy.split("/"));
    }

    public OrchidPage findPage(String[] pathPieces) {
        OrchidPage page = null;
        // we have a path to search
        if (!EdenUtils.isEmpty(pathPieces) && !EdenUtils.isEmpty(pathPieces[0])) {
            if (pathPieces.length == 1 && pathPieces[0].equals(ownKey)) {
                for (OrchidPage ownPage : getOwnPages()) {
                    if (ownPage.getReference().getOriginalFileName().equals(pathPieces[0])) {
                        page = ownPage;
                        break;
                    }
                }
            }
            else {
                if (childrenPages.containsKey(pathPieces[1])) {
                    page = childrenPages.get(pathPieces[1]).findPage(Arrays.copyOfRange(pathPieces, 1, pathPieces.length));
                }
            }
        }

        return page;
    }

    public OrchidIndex findIndex(String taxonomy) {
        return findIndex(taxonomy.split("/"));
    }

    public OrchidIndex findIndex(String[] pathPieces) {
        OrchidIndex foundIndex = null;
        // we have a path to search
        if (!EdenUtils.isEmpty(pathPieces) && !EdenUtils.isEmpty(pathPieces[0])) {
            if (pathPieces.length == 1 && pathPieces[0].equals(ownKey)) {
                foundIndex = this;
            }
            else if (childrenPages.containsKey(pathPieces[1])) {
                foundIndex = childrenPages.get(pathPieces[1]).findIndex(Arrays.copyOfRange(pathPieces, 1, pathPieces.length));
            }
        }

        return foundIndex;
    }

    public List<OrchidPage> getOwnPages() {
        return ownPages
                .stream()
                .filter((page) -> page.getContext().includeDrafts() || !page.isDraft())
                .collect(Collectors.toList());
    }

    public List<OrchidPage> getAllPages() {
        List<OrchidPage> allPages = new ArrayList<>();
        allPages.addAll(getOwnPages());

        for (Map.Entry<String, OrchidIndex> entry : childrenPages.entrySet()) {
            allPages.addAll(entry.getValue().getAllPages());
        }

        return allPages;
    }

    public void addAll(OrchidIndex index) {
        for(OrchidPage page : index.getAllPages()) {
            this.addToIndex(page.getReference().getPath(), page);
        }
    }

    public OrchidIndex get(String key) {
        return childrenPages.get(key);
    }

    public Map<String, OrchidIndex> getChildren() {
        return childrenPages;
    }

    public JSONObject toJSON() {
        return toJSON(false, false);
    }

    public JSONObject toJSON(boolean includePageContent, boolean includePageData) {
        JSONObject indexJson = new JSONObject();
        indexJson.put("ownKey", ownKey);
        List<OrchidPage> ownPages = getOwnPages();
        if (ownPages.size() > 0) {
            JSONArray ownPagesJson = new JSONArray();
            for (OrchidPage page : ownPages) {
                ownPagesJson.put(page.toJSON(includePageContent, includePageData));
            }
            indexJson.put("ownPages", ownPagesJson);
        }

        if (childrenPages.keySet().size() > 0) {
            JSONObject childrenPagesJson = new JSONObject();
            for (Map.Entry<String, OrchidIndex> childIndex : childrenPages.entrySet()) {
                childrenPagesJson.put(childIndex.getKey(), childIndex.getValue().toJSON(includePageContent, includePageData));
            }
            indexJson.put("childrenPages", childrenPagesJson);
        }

        return indexJson;
    }

    @Override
    public String toString() {
        return Clog.format("index [{}] with {} own pages and {} child indices",
                this.ownKey,
                ownPages.size(),
                childrenPages.size()
        );
    }

// Factory methods
//----------------------------------------------------------------------------------------------------------------------

    public static OrchidIndex from(String rootKey, Collection<? extends OrchidPage> pages) {
        OrchidIndex index = new OrchidIndex(null, rootKey);
        for (OrchidPage page : pages) {
            OrchidReference ref = new OrchidReference(page.getReference());
            index.addToIndex(ref.getPath(), page);
        }

        return index;
    }

    public static OrchidIndex fromJSON(OrchidContext context, JSONObject source) {
        OrchidIndex index = new OrchidIndex(null, source.getString("ownKey"));

        if (source.has("ownPages")) {
            JSONArray ownPagesJson = source.getJSONArray("ownPages");
            List<OrchidPage> ownPages = new ArrayList<>();
            for (int i = 0; i < ownPagesJson.length(); i++) {
                OrchidPage externalPage = OrchidPage.fromJSON(context, ownPagesJson.getJSONObject(i));
                ownPages.add(externalPage);
            }
            index.ownPages = ownPages;
        }

        if (source.has("childrenPages")) {
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
}
