package com.eden.orchid.options.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.AutoRegister;
import com.eden.orchid.options.SiteOption;
import com.eden.orchid.options.SiteOptions;
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
public class ResourcesOption extends SiteOption {

    private String[] dataExtensions = new String[] {"yml", "yaml", "json"};

    @Override
    public String getFlag() {
        return "resourcesDir";
    }

    @Override
    public boolean parseOption(String[] options) {
        if (options.length == 2) {
            SiteOptions.siteOptions.put("resourcesDir", options[1]);


            parseConfigFile(options[1]);
            parseDataFiles(options[1]);
            return true;
        }
        else {
            Clog.e("'-d' option should be of length 2: given #{$1}", new Object[]{options});
            return false;
        }
    }

    @Override
    public void setDefault() {

    }

    private void parseConfigFile(String resPath) {
        for(String configExtension : dataExtensions) {

            JSONObject parsedFile = parseFile(resPath, "config." + configExtension);

            if(parsedFile != null) {
                SiteOptions.siteOptions.put("config", parsedFile);
                break;
            }
        }
    }

    private void parseDataFiles(String resPath) {
        SiteOptions.siteOptions.put("data", new JSONObject());

        File dataDir = new File(resPath + File.separator + "data");

        if(dataDir.exists() && dataDir.isDirectory()) {
            List<File> files = new ArrayList<>(FileUtils.listFiles(dataDir, dataExtensions, false));

            for (File file : files) {
                JSONObject parsedFile = parseFile(file);
                if(parsedFile != null) {
                    SiteOptions.siteOptions.getJSONObject("data").put(FilenameUtils.removeExtension(file.getName()), parsedFile);
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
