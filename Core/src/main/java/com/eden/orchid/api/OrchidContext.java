package com.eden.orchid.api;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.events.FilterService;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.impl.indexing.OrchidCompositeIndex;
import com.eden.orchid.impl.indexing.OrchidExternalIndex;
import com.eden.orchid.impl.indexing.OrchidRootInternalIndex;
import com.google.inject.Injector;
import org.json.JSONObject;

import java.util.Map;

public interface OrchidContext {
    void bootstrap(Map<String, String[]> optionsMap);

    boolean runTask(String taskName);

    void build();

    JSONElement query(String pointer);

    Map<String, Object> getSiteData(Object... data);

    Theme getTheme();
    void setTheme(Theme theme);

    JSONObject getRoot();

    Injector getInjector();
    FilterService getFilterService();
    OrchidIndex getIndex();
    OrchidRootInternalIndex getInternalIndex();
    OrchidExternalIndex getExternalIndex();
    OrchidCompositeIndex getCompositeIndex();

    void broadcast(String event, Object... args);
}
