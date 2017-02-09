package com.eden.orchid.impl.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.options.OrchidOption;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Singleton
public class ConfigOption implements OrchidOption {
    private String[] dataExtensions = new String[] {"yml", "yaml", "json"};

    private JSONObject configFile;

    @Override
    public String getFlag() {
        return "config";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public JSONElement parseOption(String[] options) {
        JSONElement res = Orchid.getContext().query("options.resourcesDir");
        if(res != null) {
            parseConfigFile(res.toString());
            applyConfigFile();

            return new JSONElement(configFile);
        }
        else {
            return null;
        }
    }

    @Override
    public JSONElement getDefaultValue() {
        return null;
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public int optionLength() {
        return 0;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    private void parseConfigFile(String resPath) {

        JSONObject configOptions = new JSONObject();
        String env = "";

        if(Orchid.getContext().query("options.environment") != null) {
            env = Orchid.getContext().query("options.environment").toString();
        }

        for(String configExtension : dataExtensions) {
            JSONObject parsedFile = parseFile(resPath, "config-" + env + "." + configExtension);
            if(parsedFile != null) {
                for(String key : parsedFile.keySet()) {
                    configOptions.put(key, parsedFile.get(key));
                }
                break;
            }

            parsedFile = parseFile(resPath, "config." + configExtension);
            if(parsedFile != null) {
                for(String key : parsedFile.keySet()) {
                    configOptions.put(key, parsedFile.get(key));
                }
                break;
            }
        }

        configFile = configOptions;
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

    private void applyConfigFile() {
        if(configFile != null && configFile.length() > 0) {
            JSONObject options = (JSONObject) Orchid.getContext().query("options").getElement();
            for(String key : configFile.keySet()) {
                options.put(key, configFile.get(key));
            }
        }
    }
}
