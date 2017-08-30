package com.eden.orchid.api.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OptionsServiceImpl implements OptionsService {

    private OrchidContext context;
    private String environment;

    private String[] formats = new String[]{"config-#{$1}", "config"};

    private JSONObject optionsData;

    @Inject
    public OptionsServiceImpl() {
        this.environment = OrchidFlags.getInstance().getString("environment");
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
            optionsData = loadOptions();
        }
        return optionsData;
    }

    @Override
    public JSONObject loadOptions() {
        JSONObject options = new JSONObject();

        JSONObject dataFiles = context.getDatafiles("data");
        if (dataFiles != null) {
            options.put("data", dataFiles);
            for (String key : dataFiles.keySet()) {
                options.put(key, dataFiles.get(key));
            }
        }

        JSONObject configOptions = loadConfigFile();
        if (configOptions != null) {
            options.put("config", configOptions);
            for (String key : configOptions.keySet()) {
                options.put(key, configOptions.get(key));
            }
        }

        return options;
    }

    @Override
    public JSONObject loadConfigFile() {
        return Arrays.stream(formats)
                     .map(format -> context.getDatafile(Clog.format(format, environment)))
                     .filter(Objects::nonNull)
                     .findFirst()
                     .orElse(null);
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

        siteData.put("index", context.getIndex());
        siteData.put("options", context.getOptionsData().toMap());
        siteData.put("theme", context.getTheme());

        return siteData;
    }
}
