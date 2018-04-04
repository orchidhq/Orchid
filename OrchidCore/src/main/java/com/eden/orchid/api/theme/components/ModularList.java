package com.eden.orchid.api.theme.components;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModularList<L extends ModularList<L, I>, I extends ModularListItem<L, I>> {

    protected final OrchidContext context;
    protected final Class<I> itemClass;
    protected final JSONArray itemsJson;
    protected final Map<String, Class<I>> itemTypes;

    protected List<I> loadedItems;

    public ModularList(OrchidContext context, Class<I> itemClass, JSONArray itemsJson) {
        this.context = context;
        this.itemClass = itemClass;
        this.itemsJson = itemsJson;

        Set<I> itemTypes = context.resolveSet(itemClass);
        this.itemTypes = new HashMap<>();

        for (I itemType : itemTypes) {
            this.itemTypes.put(itemType.getType(), (Class<I>) itemType.getClass());
        }
    }

    public ModularList(OrchidContext context, Class<I> itemClass, Map<String, Class<I>> itemTypesMap, JSONArray itemsJson) {
        this.context = context;
        this.itemClass = itemClass;
        this.itemsJson = itemsJson;
        this.itemTypes = itemTypesMap;
    }

    public boolean isEmpty() {
        if (loadedItems != null) {
            if(loadedItems.size() > 0) {
                return false;
            }
        }
        else if (itemsJson != null) {
            if(itemsJson.length() > 0) {
                return false;
            }
        }

        return true;
    }

    public List<I> get(OrchidPage containingPage) {
        if (loadedItems == null) {
            loadedItems = new ArrayList<>();

            for (int i = 0; i < itemsJson.length(); i++) {
                JSONObject itemJson = itemsJson.getJSONObject(i);
                String itemType = itemJson.optString("type");

                if(!EdenUtils.isEmpty(itemType)) {
                    if (itemTypes.containsKey(itemType)) {
                        I item = context.getInjector().getInstance(itemTypes.get(itemType));
                        item.setOrder((i + 1) * 10);

                        if (item.canBeUsedOnPage(containingPage, (L) this, itemsJson, loadedItems)) {
                            item.setPage(containingPage);
                            item.extractOptions(context, itemJson);
                            loadedItems.add(item);
                        }
                    }
                    else {
                        Clog.w("{} type {} could not be found (on page {} at {})", itemClass.getSimpleName(), itemType, containingPage.getTitle(), containingPage.getLink());
                    }
                }
                else {
                    Clog.w("{} type not given (on page {} at {})", itemClass.getSimpleName(), itemType, containingPage.getTitle(), containingPage.getLink());
                }
            }

            loadedItems.sort(Comparator.comparingInt(ModularListItem::getOrder));
        }

        return loadedItems;
    }

    public void invalidate() {
        loadedItems = null;
    }

    public void add(JSONObject menuItemJson) {
        invalidate();
        itemsJson.put(menuItemJson);
    }

    public void add(JSONArray menuItemsJson) {
        invalidate();
        for (int i = 0; i < menuItemsJson.length(); i++) {
            itemsJson.put(menuItemsJson.get(i));
        }
    }
}
