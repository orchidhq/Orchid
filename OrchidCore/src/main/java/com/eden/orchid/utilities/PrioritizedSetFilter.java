package com.eden.orchid.utilities;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.registration.Prioritized;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrioritizedSetFilter<T extends Prioritized> {

    private static final String ALLOWED_KEY = "enabled";
    private static final String DISABLED_KEY = "disabled";

    private OrchidContext context;
    private final String key;
    private final ObservableTreeSet<T> originalSet;
    private ObservableTreeSet<T> fullFilteredSet;

    public PrioritizedSetFilter(OrchidContext context, String key, Set<T> originalSet) {
        if(EdenUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key must not be empty");
        }

        this.context = context;
        this.key = key;
        this.originalSet = new ObservableTreeSet<>(originalSet);
    }

    public Set<T> getFilteredSet() {
        if(fullFilteredSet != null) {
            return fullFilteredSet;
        }
        JSONElement element = context.query("options." + key);

        Stream<T> filter = originalSet.stream();

        if(OrchidUtils.elementIsObject(element)) {
            JSONObject filterQuery = (JSONObject) element.getElement();

            if(filterQuery.has(ALLOWED_KEY) && filterQuery.get(ALLOWED_KEY) instanceof JSONArray) {
                JSONArray allowedItems = filterQuery.getJSONArray(ALLOWED_KEY);

                if (allowedItems.length() > 0) {
                    Clog.v("PrioritizedSetFilter '#{$1}' applying filters to whitelist items", key);

                    filter = filter.filter(t -> {
                        boolean inAllowedItems = false;

                        for (int i = 0; i < allowedItems.length(); i++) {
                            inAllowedItems = t.getClass().getSimpleName().equals(allowedItems.getString(i));
                            inAllowedItems = inAllowedItems || (t.getClass().getName().equals(allowedItems.getString(i)));

                            if (inAllowedItems) {
                                break;
                            }
                        }

                        return inAllowedItems;
                    });
                }
            }

            if(filterQuery.has(DISABLED_KEY) && filterQuery.get(DISABLED_KEY) instanceof JSONArray) {
                JSONArray disabledItems = filterQuery.getJSONArray(DISABLED_KEY);

                if (disabledItems.length() > 0) {
                    Clog.v("PrioritizedSetFilter '#{$1}' applying filters to blacklist items", key);
                    filter = filter.filter(t -> {
                        boolean inDisallowedItems = false;

                        for (int i = 0; i < disabledItems.length(); i++) {
                            inDisallowedItems = t.getClass().getSimpleName().equals(disabledItems.getString(i));
                            inDisallowedItems = inDisallowedItems || (t.getClass().getName().equals(disabledItems.getString(i)));

                            if (inDisallowedItems) {
                                break;
                            }
                        }

                        return !inDisallowedItems;
                    });
                }
            }
        }

        Set<T> initialFilteredSet = filter.collect(Collectors.toSet());
        fullFilteredSet = new ObservableTreeSet<>(initialFilteredSet);

        return fullFilteredSet;
    }
}
