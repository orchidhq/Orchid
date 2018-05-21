package com.eden.orchid.api.options;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.*;

/**
 * @since v1.0.0
 * @orchidApi services
 */
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
                optionsData = EdenUtils.merge(optionsData, dataFiles);
            }

            JSONObject configOptions = loadConfigFile();
            if (configOptions != null) {
                optionsData = EdenUtils.merge(optionsData, configOptions);
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
                addMap(siteData, page.getMap());
            }
            else if (data instanceof JSONElement) {
                addJSONElement(siteData, (JSONElement) data);
            }
            else if (data instanceof JSONObject) {
                addJSONObject(siteData, (JSONObject) data);
            }
            else if (data instanceof Map) {
                addMap(siteData, (Map<String, ?>) data);
            }
            else if (data instanceof JSONArray) {
                addJSONArray(siteData, (JSONArray) data);
            }
            else if (data instanceof Collection) {
                addCollection(siteData, (Collection) data);
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
            allConfig = EdenUtils.merge(allConfig, config);
        }

        JSONObject envConfig = context.getDatafile("config-" + context.getEnvironment());
        if(envConfig != null) {
            allConfig = EdenUtils.merge(allConfig, envConfig);
        }

        return allConfig;
    }

    private JSONObject loadDataFiles() {
        return context.getDatafiles("data");
    }

// Helpers for getting data from object types
//----------------------------------------------------------------------------------------------------------------------

    private void addJSONElement(Map<String, Object> siteData, JSONElement data) {
        if(EdenUtils.elementIsObject(data)) {
            addJSONObject(siteData, (JSONObject) data.getElement());
        }
        else if(EdenUtils.elementIsArray(data)) {
            addJSONArray(siteData, (JSONArray) data.getElement());
        }
    }

    private void addJSONObject(Map<String, Object> siteData, JSONObject data) {
        addMap(siteData, data.toMap());
    }

    private void addJSONArray(Map<String, Object> siteData, JSONArray data) {
        addCollection(siteData, data.toList());
    }

    private void addMap(Map<String, Object> siteData, Map<String, ?> data) {
        for(Map.Entry<String, ?> entry : data.entrySet()) {
            siteData.put(entry.getKey(), entry.getValue());
        }
        siteData.put("data", data);
    }

    private void addCollection(Map<String, Object> siteData, Collection<?> data) {
        siteData.put("data", data);
    }

}
