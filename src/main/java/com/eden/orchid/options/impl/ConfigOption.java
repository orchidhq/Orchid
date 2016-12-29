package com.eden.orchid.options.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.AutoRegister;
import com.eden.orchid.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.options.Option;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@AutoRegister
public class ConfigOption implements Option {
    private String[] dataExtensions = new String[] {"yml", "yaml", "json"};

    @Override
    public String getFlag() {
        return "config";
    }

    @Override
    public JSONElement parseOption(String[] options) {
        return parseConfigFile(Orchid.query("options.resourcesDir").toString());
    }

    @Override
    public JSONElement getDefaultValue() {
        return null;
    }

    @Override
    public int priority() {
        return 89;
    }

    @Override
    public int optionLength() {
        return 0;
    }

    private JSONElement parseConfigFile(String resPath) {

        JSONObject configOptions = new JSONObject();

        for(String configExtension : dataExtensions) {
            Clog.d("Trying to parse config file: config.#{$1}", new Object[]{configExtension});

            JSONObject parsedFile = parseFile(resPath, "config." + configExtension);

            if(parsedFile != null) {
                Clog.d("Found valid config file");
                for(String key : parsedFile.keySet()) {
                    configOptions.put(key, parsedFile.get(key));
                }

                break;
            }
        }

        return new JSONElement(configOptions);
    }

    private JSONObject parseFile(String resPath, String fileName) {
        return parseFile(new File(resPath + File.separator + fileName));
    }

    private JSONObject parseFile(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }

        switch (FilenameUtils.getExtension(file.getName()).toLowerCase()) {
            case "json":
                return parseJsonFile(file);
            case "yaml":
            case "yml":
                return parseYamlFile(file);
            default:
                return null;
        }
    }

    private JSONObject parseJsonFile(File file) {
        try {
            return new JSONObject(IOUtils.toString(new FileInputStream(file), "UTF-8"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private JSONObject parseYamlFile(File file) {
        try {
            return new JSONObject((Map<String, Object>) new Yaml().load( new FileInputStream(file)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
