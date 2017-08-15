package com.eden.orchid.api;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.compilers.CompilerService;
import com.eden.orchid.api.events.EventService;
import com.eden.orchid.api.indexing.IndexService;
import com.eden.orchid.api.resources.ResourceService;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.ThemeService;
import com.google.inject.Injector;
import org.json.JSONObject;

import java.util.Map;

public interface OrchidContext extends
        CompilerService,
        ThemeService,
        EventService,
        IndexService,
        ResourceService {

    boolean runTask(Theme defaultTheme, String taskName);
    void build();
    Injector getInjector();

    JSONObject getRoot();
    JSONElement query(String pointer);
    Map<String, Object> getSiteData(Object... data);
}
