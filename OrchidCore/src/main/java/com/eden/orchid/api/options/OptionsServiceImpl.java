package com.eden.orchid.api.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Arrays;
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
        if(optionsData == null) {
            optionsData = loadOptions();
        }
        return optionsData;
    }

    @Override
    public JSONObject loadOptions() {
        JSONObject options = new JSONObject();

        JSONObject configOptions = loadConfigFile();
        JSONObject dataFiles = context.getLocalDatafiles("data");

        options.put("data", dataFiles);
        for (String key : dataFiles.keySet()) {
            options.put(key, dataFiles.get(key));
        }

        options.put("config", configOptions);
        for (String key : configOptions.keySet()) {
            options.put(key, configOptions.get(key));
        }

        return options;
    }

    @Override
    public JSONObject loadConfigFile() {
        return Arrays.stream(formats)
                     .map(format -> context.getLocalDatafile(Clog.format(format, environment)))
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
}
