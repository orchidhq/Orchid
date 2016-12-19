package com.eden.orchid.options.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.AutoRegister;
import com.eden.orchid.options.Option;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AutoRegister
public class ResourcesOption implements Option {

    private String[] dataExtensions = new String[] {"yml", "yaml", "json"};

    @Override
    public String getFlag() {
        return "resourcesDir";
    }

    @Override
    public boolean parseOption(JSONObject siteOptions, String[] options) {
        if (options.length == 2) {
            siteOptions.put("resourcesDir", options[1]);
            parseConfigFile(siteOptions, options[1]);
            parseDataFiles(siteOptions, options[1]);
            return true;
        }
        else {
            Clog.e("'-resourcesDir' option should be of length 2: given #{$1}", new Object[]{options});
            return false;
        }
    }

    @Override
    public void setDefault(JSONObject siteOptions) {

    }

    @Override
    public int priority() {
        return 90;
    }

    private void parseConfigFile(JSONObject siteOptions, String resPath) {
        for(String configExtension : dataExtensions) {

            JSONObject parsedFile = parseFile(resPath, "config." + configExtension);

            if(parsedFile != null) {
                siteOptions.put("config", parsedFile);
                break;
            }
        }
    }

    private void parseDataFiles(JSONObject siteOptions, String resPath) {
        siteOptions.put("data", new JSONObject());

        File dataDir = new File(resPath + File.separator + "data");

        if(dataDir.exists() && dataDir.isDirectory()) {
            List<File> files = new ArrayList<>(FileUtils.listFiles(dataDir, dataExtensions, false));

            for (File file : files) {
                JSONObject parsedFile = parseFile(file);
                if(parsedFile != null) {
                    siteOptions.getJSONObject("data").put(FilenameUtils.removeExtension(file.getName()), parsedFile);
                    break;
                }
            }
        }
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
