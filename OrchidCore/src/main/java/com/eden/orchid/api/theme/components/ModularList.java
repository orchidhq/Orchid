package com.eden.orchid.api.theme.components;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.google.inject.Provider;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ModularList<L extends ModularList<L, I>, I extends ModularListItem<L, I>> {

    protected final OrchidContext context;
    private final Provider<Map<String, Class<I>>> itemTypesProvider;

    private Map<String, Class<I>> itemTypes;

    protected JSONArray itemsJson;
    protected List<I> loadedItems;

    private boolean initialized = false;

    @Inject
    public ModularList(OrchidContext context) {
        this.context = context;
        this.itemTypesProvider = () -> {
            Set<I> itemTypes = context.resolveSet(getItemClass());
            HashMap<String, Class<I>> itemTypesMap = new HashMap<>();
            for (I itemType : itemTypes) {
                itemTypesMap.put(itemType.getType(), (Class<I>) itemType.getClass());
            }

            return itemTypesMap;
        };
    }

    public ModularList(OrchidContext context, Provider<Map<String, Class<I>>> itemTypesProvider) {
        this.context = context;
        this.itemTypesProvider = itemTypesProvider;
    }

    protected abstract Class<I> getItemClass();

    public void initialize(JSONArray itemsJson) {
        if(!initialized) {
            this.itemTypes = itemTypesProvider.get();
            this.itemsJson = itemsJson;
            initialized = true;
        }
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

    public List<I> get() {
        if (loadedItems == null) {
            loadedItems = new ArrayList<>();

            for (int i = 0; i < itemsJson.length(); i++) {
                JSONObject itemJson = itemsJson.getJSONObject(i);
                String itemType = itemJson.optString("type");

                if(!EdenUtils.isEmpty(itemType)) {
                    if (itemTypes.containsKey(itemType)) {
                        I item = context.getInjector().getInstance(itemTypes.get(itemType));
                        item.setOrder((i + 1) * 10);
                        item.extractOptions(context, itemJson);
                        addItem(item, itemJson);
                    }
                    else {
                        Clog.w("{} type [{}] could not be found {}", getItemClass().getSimpleName(), itemType, getLogMessage());
                    }
                }
                else {
                    Clog.w("{} type not given {}", getItemClass().getSimpleName(), itemType, getLogMessage());
                }
            }

            loadedItems.sort(Comparator.comparingInt(ModularListItem::getOrder));
        }

        return loadedItems;
    }

    protected void addItem(I item, JSONObject itemJson) {
        loadedItems.add(item);
    }

    protected String getLogMessage() {
        return "";
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
