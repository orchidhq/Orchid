package com.eden.orchid.impl.events;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.events.On;
import com.eden.orchid.utilities.OrchidUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;

public class SetupEnvironment implements EventListener {
    private String[] dataExtensions = new String[] {"yml", "yaml", "json", "csv", "tsv"};
    private String[] formats = new String[] {"config-#{$1}.#{$2}", "config.#{$2}"};

    private OrchidContext context;

    @Inject
    public SetupEnvironment(OrchidContext context) {
        this.context = context;
    }

    @On(Orchid.Events.OPTIONS_PARSED)
    public void onOptionsComplete(JSONObject options) {
        parseConfigFile(options);
        parseDataFiles(options);
        context.broadcast(Orchid.Events.DATAFILES_PARSED, options);
    }

    // config file
    private void parseConfigFile(JSONObject options) {
        String resPath = "" + options.optString("resourcesDir");
        String env = "" + options.optString("environment");

        JSONObject configFile = Arrays.stream(dataExtensions)
                .map(ext ->
                    Arrays.stream(formats)
                          .map(format -> parseFile(new File(resPath + "/" + Clog.format(format, env, ext))))
                          .filter(OrchidUtils::elementIsObject)
                          .map(el -> (JSONObject) el.getElement())
                          .findFirst()
                          .orElseGet(() -> null)
                )
                .findFirst()
                .orElseGet(() -> new JSONObject());

        if(configFile.length() > 0) {
            for(String key : configFile.keySet()) {
                options.put(key, configFile.get(key));
            }
        }

        options.put("config", configFile);
    }

    private void parseDataFiles(JSONObject options) {
        String resPath = "" + options.optString("resourcesDir");

        JSONObject dataOptions = new JSONObject();

        File dataDir = new File(resPath + "/data");

        if(dataDir.exists() && dataDir.isDirectory()) {
            new ArrayList<File>(FileUtils.listFiles(dataDir, dataExtensions, false))
                     .stream()
                     .forEach(file -> {
                         JSONElement parsedFile = parseFile(file);
                         if(parsedFile != null) {
                             dataOptions.put(FilenameUtils.removeExtension(file.getName()), parsedFile.getElement());
                         }
                     });
        }

        options.put("data", dataOptions);
    }

    private JSONElement parseFile(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }

        try {
            return context.getTheme().parse(FilenameUtils.getExtension(file.getName()), IOUtils.toString(new FileInputStream(file)));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
