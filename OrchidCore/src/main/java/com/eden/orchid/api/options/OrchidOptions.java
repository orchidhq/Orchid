package com.eden.orchid.api.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidResources;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class OrchidOptions {

    private OrchidContext context;
    private Set<OrchidGenerator> generators;
    private Set<OrchidParser> parsers;
    private OrchidResources resources;
    private String environment;

    private String[] formats = new String[]{"config-#{$1}", "config"};

    @Inject
    public OrchidOptions(
            OrchidContext context,
            Set<OrchidGenerator> generators,
            Set<OrchidParser> parsers,
            OrchidResources resources) {
        this.context = context;
        this.generators = generators;
        this.parsers = parsers;
        this.resources = resources;
        this.environment = OrchidFlags.getInstance().getString("environment");
    }

    public JSONObject loadOptions() {
        JSONObject options = new JSONObject();

        JSONObject configOptions = loadConfigFile();
        JSONObject dataFiles = resources.getLocalDatafiles("data");

        options.put("data", dataFiles);
        for (String key : dataFiles.keySet()) {
            options.put(key, dataFiles.get(key));
        }

        options.put("config", configOptions);
        for (String key : configOptions.keySet()) {
            options.put(key, configOptions.get(key));
        }

        // TODO: Extract options into generator

        return options;
    }

    public JSONObject loadConfigFile() {
        return Arrays.stream(formats)
                     .map(format -> resources.getLocalDatafile(Clog.format(format, environment)))
                     .filter(Objects::nonNull)
                     .findFirst()
                     .orElse(null);
    }

    public JSONObject loadGeneratorOptions(OrchidGenerator generator) {
        return new JSONObject();
    }
}
