package com.eden.orchid.api;

import com.eden.common.json.JSONElement;
import com.eden.orchid.Theme;
import com.eden.orchid.api.events.FilterService;
import com.eden.orchid.api.generators.OrchidIndex;
import com.google.inject.Injector;
import com.sun.javadoc.RootDoc;
import org.json.JSONObject;

import java.util.Map;

public interface OrchidContext {
    void bootstrap(Map<String, String[]> optionsMap, RootDoc rootDoc);

    boolean runTask(String taskName);

    void build();

    JSONElement query(String pointer);

    Map<String, Object> getSiteData(Object... data);

    Theme getTheme();
    void setTheme(Theme theme);

    JSONObject getRoot();

    RootDoc getRootDoc();
    Injector getInjector();
    FilterService getFilterService();
    OrchidIndex getIndex();

    void broadcast(String event, Object... args);
}
