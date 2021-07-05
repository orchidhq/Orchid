package com.eden.orchid.api.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidService;
import com.google.inject.ImplementedBy;

import java.util.Map;

@ImplementedBy(OptionsServiceImpl.class)
public interface OptionsService extends OrchidService {

    default void clearOptions() {
        getService(OptionsService.class).clearOptions();
    }

    default Map<String, Object> loadOptions() {
        return getService(OptionsService.class).loadOptions();
    }

    default Map<String, Object> getConfig() {
        return getService(OptionsService.class).getConfig();
    }

    default Map<String, Object> getData() {
        return getService(OptionsService.class).getData();
    }

    default JSONElement query(String pointer) {
        return getService(OptionsService.class).query(pointer);
    }

    default Map<String, Object> getSiteData(Object data) {
        return getService(OptionsService.class).getSiteData(data);
    }

}
