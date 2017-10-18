package com.eden.orchid.api.options;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class OptionsServiceImpl implements OptionsService {

    private OrchidContext context;

    private String[] formats = new String[]{"config-#{$1}", "config"};

    private JSONObject optionsData;

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    @Override
    public void clearOptions() {
        optionsData = null;
    }

    @Override
    public JSONObject getOptionsData() {
        if (optionsData == null) {
            loadOptions();
        }
        return optionsData;
    }

    @Override
    public JSONObject loadOptions() {
        if (optionsData == null) {
            optionsData = new JSONObject();

            JSONObject dataFiles = context.getDatafiles("data");
            if (dataFiles != null) {
                optionsData.put("data", dataFiles);
                for (String key : dataFiles.keySet()) {
                    optionsData.put(key, dataFiles.get(key));
                }
            }

            JSONObject configOptions = loadConfigFile();
            if (configOptions != null) {
                optionsData.put("config", configOptions);
                for (String key : configOptions.keySet()) {
                    optionsData.put(key, configOptions.get(key));
                }
            }
        }

        return optionsData;
    }

    @Override
    public JSONObject loadConfigFile() {
        JSONObject allConfig = new JSONObject();

        JSONObject config = context.getDatafile("config");
        if(config != null) {
            for(String key : config.keySet()) {
                allConfig.put(key, config.get(key));
            }
        }

        JSONObject envConfig = context.getDatafile("config-" + context.getEnvironment());
        if(envConfig != null) {
            for(String key : envConfig.keySet()) {
                allConfig.put(key, envConfig.get(key));
            }
        }

        return allConfig;
    }

    @Override
    public JSONElement query(String pointer) {
        if (!EdenUtils.isEmpty(pointer)) {
            return new JSONElement(getOptionsData()).query(pointer);
        }
        return null;
    }

    @Override
    public Map<String, Object> getSiteData(Object... data) {
        Map<String, Object> siteData = new HashMap<>();

        if (data != null && data.length > 0) {
            if (data[0] instanceof OrchidPage) {
                OrchidPage page = (OrchidPage) data[0];
                siteData.put("page", page);
                siteData.put(page.getKey(), page);
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

        siteData.put("site", context.getSite());
        siteData.put("index", context.getIndex());
        siteData.put("options", context.getOptionsData().toMap());
        siteData.put("theme", context.getTheme());

        return siteData;
    }
}
