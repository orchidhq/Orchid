package com.eden.orchid.api.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidService;
import org.json.JSONObject;

import java.util.Map;

public interface OptionsService extends OrchidService {

    default void clearOptions() {
        getService(OptionsService.class).clearOptions();
    }

    default JSONObject getOptionsData() {
        return getService(OptionsService.class).getOptionsData();
    }

    default JSONObject loadOptions() {
        return getService(OptionsService.class).loadOptions();
    }

    default JSONObject loadConfigFile() {
        return getService(OptionsService.class).loadConfigFile();
    }

    default JSONElement query(String pointer) {
        return getService(OptionsService.class).query(pointer);
    }

    default Map<String, Object> getSiteData(Object... data) {
        return getService(OptionsService.class).getSiteData(data);
    }

}
