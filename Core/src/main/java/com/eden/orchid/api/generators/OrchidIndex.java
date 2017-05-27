package com.eden.orchid.api.generators;

import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class OrchidIndex {

    protected final String ownKey;
    protected List<OrchidPage> ownPages;
    protected Map<String, OrchidIndex> childrenPages;

    public OrchidIndex(String ownKey) {
        this.ownKey = ownKey;
        this.ownPages = new ArrayList<>();
        this.childrenPages = new HashMap<>();
    }

    public void addToTaxonomy(String taxonomy, OrchidPage page) {
        String[] pathPieces = taxonomy.split(File.separator);

        if(pathPieces[0].length() > 0) {
            if(pathPieces.length == 1) {
                if(pathPieces[0].equals(ownKey)) {
                    this.ownPages.add(page);
                }
            }
            else {
                String innermoreKey = "";
                for (int i = 1; i < pathPieces.length; i++) {
                    innermoreKey += pathPieces[i] + "/";
                }
                innermoreKey = OrchidUtils.stripSeparators(innermoreKey);

                childrenPages.putIfAbsent(pathPieces[1], new OrchidIndex(pathPieces[1]));
                childrenPages.get(pathPieces[1]).addToTaxonomy(innermoreKey, page);
            }
        }
    }

    public List<OrchidPage> find(String taxonomy) {
        List<OrchidPage> foundPages = new ArrayList<>();
        String[] pathPieces = taxonomy.split(File.separator);
        if(pathPieces[0].length() > 0) {
            if(pathPieces.length == 1) {
                if(pathPieces[0].equals(ownKey)) {
                    foundPages.addAll(this.getAllPages());
                }
            }
            else {
                String innermoreKey = "";
                for (int i = 1; i < pathPieces.length; i++) {
                    innermoreKey += pathPieces[i] + "/";
                }
                innermoreKey = OrchidUtils.stripSeparators(innermoreKey);

                if(childrenPages.containsKey(pathPieces[1])) {
                    foundPages.addAll(childrenPages.get(pathPieces[1]).find(innermoreKey));
                }
            }
        }

        return foundPages;
    }

    public List<OrchidPage> getAllPages() {
        List<OrchidPage> allPages = new ArrayList<>();
        allPages.addAll(ownPages);

        for(Map.Entry<String, OrchidIndex> entry : childrenPages.entrySet()) {
            allPages.addAll(entry.getValue().getAllPages());
        }

        return allPages;
    }
}
