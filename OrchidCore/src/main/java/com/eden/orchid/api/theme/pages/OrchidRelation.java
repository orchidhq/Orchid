package com.eden.orchid.api.theme.pages;

import com.eden.orchid.api.OrchidContext;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public final class OrchidRelation {

    private final OrchidContext context;
    private final JSONArray relatedItemsJson;

    private List<Object> relatedItems;

    @Inject
    public OrchidRelation(OrchidContext context, JSONArray relatedItemsJson) {
        this.context = context;
        this.relatedItemsJson = relatedItemsJson;
    }

    public boolean isEmpty() {
        if (relatedItems != null) {
            if(relatedItems.size() > 0) {
                return false;
            }
        }
        else if (relatedItemsJson != null) {
            if(relatedItemsJson.length() > 0) {
                return false;
            }
        }

        return true;
    }

    public List<?> get() {
        if (relatedItems == null) {
            relatedItems = new ArrayList<>();

            for (int i = 0; i < relatedItemsJson.length(); i++) {
                String relatedItemId = relatedItemsJson.getString(i);
                List<?> relatedPages = context.findAllInCollection(relatedItemId);
                if(relatedPages != null) {
                    relatedItems.addAll(relatedPages);
                }
            }
        }

        return relatedItems;
    }

    public void invalidateRelatedItems() {
        relatedItems = null;
    }
}

