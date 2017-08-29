package com.eden.orchid.impl;

import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.compilers.CompilerService;
import com.eden.orchid.api.events.EventService;
import com.eden.orchid.api.indexing.IndexService;
import com.eden.orchid.api.options.OptionsService;
import com.eden.orchid.api.resources.ResourceService;
import com.eden.orchid.api.tasks.TaskService;
import com.eden.orchid.api.theme.ThemeService;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.google.inject.Injector;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
@Getter
public final class OrchidContextImpl implements OrchidContext {

    private Injector injector;

    private Map<Class<? extends OrchidService>, OrchidService> services;

    @Inject
    public OrchidContextImpl(
            Injector injector,

            CompilerService compilerService,
            ThemeService themeService,
            EventService eventService,
            IndexService indexService,
            ResourceService resourceService,
            TaskService taskService,
            OptionsService optionsService,
            Set<OrchidService> additionalServices
    ) {
        this.injector = injector;

        services = new HashMap<>();
        initializeService(CompilerService.class, compilerService);
        initializeService(ThemeService.class, themeService);
        initializeService(EventService.class, eventService);
        initializeService(IndexService.class, indexService);
        initializeService(ResourceService.class, resourceService);
        initializeService(TaskService.class, taskService);
        initializeService(OptionsService.class, optionsService);

        for(OrchidService service : additionalServices) {
            services.put(service.getClass(), service);
        }

        initialize(this);
    }

// Service Delegation
//----------------------------------------------------------------------------------------------------------------------

    private <T extends OrchidService> void initializeService(Class<T> serviceClass, T service) {
        services.put(serviceClass, service);
    }

    @Override
    public void initialize(OrchidContext context) {
        services.values().forEach(service -> service.initialize(context));
        broadcast(Orchid.Events.INIT_COMPLETE, this);
    }

    @Override
    public void start() {
        services.values().forEach(OrchidService::onStart);
        broadcast(Orchid.Events.ON_START, this);
    }

    @Override
    public void finish() {
        broadcast(Orchid.Events.ON_FINISH, this);
        services.values().forEach(OrchidService::onFinish);
        broadcast(Orchid.Events.SHUTDOWN, this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends OrchidService> T getService(Class<T> serviceClass) {
        return (T) services.get(serviceClass);
    }

// Other
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Map<String, Object> getSiteData(Object... data) {
        Map<String, Object> siteData = new HashMap<>();

        if (data != null && data.length > 0) {
            if (data[0] instanceof OrchidPage) {
                OrchidPage page = (OrchidPage) data[0];
                siteData.put("page", page);
                siteData.put(page.getKey(), page);
            }
            else if (data[0] instanceof OrchidComponent) {
                OrchidComponent component = (OrchidComponent) data[0];
                siteData.put("component", component);
                siteData.put(component.getKey(), component);
            }
            else if (data[0] instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) data[0];
                siteData.put("data", jsonObject);

                for (String key : jsonObject.keySet()) {
                    siteData.put(key, jsonObject.get(key));
                }
            }
            else if (data[0] instanceof Map) {
                Map<String, ?> map = (Map<String, ?>) data[0];
                siteData.put("data", map);

                for (String key : map.keySet()) {
                    siteData.put(key, map.get(key));
                }
            }
            else if (data[0] instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) data[0];
                siteData.put("data", jsonArray);
            }
            else if (data[0] instanceof Collection) {
                Collection collection = (Collection) data[0];
                siteData.put("data", collection);
            }
        }

        siteData.put("index", getService(IndexService.class).getIndex());
        siteData.put("options", getService(OptionsService.class).getOptionsData().toMap());
        siteData.put("theme", getService(ThemeService.class).getTheme());

        return siteData;
    }
}
