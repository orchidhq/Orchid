package com.eden.orchid.api.options;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class OptionsServiceImpl implements OptionsService {

    private OrchidContext context;
    private Set<TemplateGlobal> globals;

    private JSONObject optionsData;

    @Inject
    public OptionsServiceImpl(Set<TemplateGlobal> globals) {
        this.globals = globals;
    }

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

            JSONObject dataFiles = loadDataFiles();
            if (dataFiles != null) {
                optionsData = OrchidUtils.merge(optionsData, dataFiles);
            }

            JSONObject configOptions = loadConfigFile();
            if (configOptions != null) {
                optionsData = OrchidUtils.merge(optionsData, configOptions);
            }
        }

        return optionsData;
    }

    @Override
    public JSONElement query(String pointer) {
        if (!EdenUtils.isEmpty(pointer)) {
            return new JSONElement(getOptionsData()).query(pointer);
        }
        return null;
    }

    @Override
    public Map<String, Object> getSiteData(Object data) {
        Map<String, Object> siteData = new HashMap<>();

        if (data != null) {
            if (data instanceof OrchidPage) {
                OrchidPage page = (OrchidPage) data;
                siteData.put("page", page);
                siteData.put(page.getKey(), page);
            }
            else if (data instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) data;
                siteData.put("data", jsonObject);

                for (String key : jsonObject.keySet()) {
                    siteData.put(key, jsonObject.get(key));
                }
            }
            else if (data instanceof Map) {
                Map<String, ?> map = (Map<String, ?>) data;
                siteData.put("data", map);

                for (String key : map.keySet()) {
                    siteData.put(key, map.get(key));
                }
            }
            else if (data instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) data;
                siteData.put("data", jsonArray);
            }
            else if (data instanceof Collection) {
                Collection collection = (Collection) data;
                siteData.put("data", collection);
            }
        }

        for(TemplateGlobal global : globals) {
            siteData.put(global.key(), global.get(context));
        }

        return siteData;
    }

    private JSONObject loadConfigFile() {
        JSONObject allConfig = new JSONObject();

        JSONObject config = context.getDatafile("config");
        if(config != null) {
            allConfig = OrchidUtils.merge(allConfig, config);
        }

        JSONObject envConfig = context.getDatafile("config-" + context.getEnvironment());
        if(envConfig != null) {
            allConfig = OrchidUtils.merge(allConfig, envConfig);
        }

        return allConfig;
    }

    private JSONObject loadDataFiles() {
        return context.getDatafiles("data");
    }
}
