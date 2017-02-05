package com.eden.orchid.impl.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.options.Option;
import com.eden.orchid.utilities.AutoRegister;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AutoRegister
public class DataFilesOption implements Option {
    private String[] dataExtensions = new String[] {"yml", "yaml", "json"};

    @Override
    public String getFlag() {
        return "data";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public JSONElement parseOption(String[] options) {
        JSONElement res = Orchid.query("options.resourcesDir");
        if(res != null) {
            return parseDataFiles(res.toString());
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
        return 60;
    }

    @Override
    public int optionLength() {
        return 0;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    private JSONElement parseDataFiles(String resPath) {
        JSONObject dataOptions = new JSONObject();

        File dataDir = new File(resPath + File.separator + "data");

        if(dataDir.exists() && dataDir.isDirectory()) {
            List<File> files = new ArrayList<>(FileUtils.listFiles(dataDir, dataExtensions, false));

            for (File file : files) {
                JSONElement parsedFile = parseFile(file);
                if(parsedFile != null) {
                    dataOptions.put(FilenameUtils.removeExtension(file.getName()), parsedFile.getElement());
                }
            }
        }

        return new JSONElement(dataOptions);
    }

    private JSONElement parseFile(File file) {
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

    private JSONElement parseJsonFile(File file) {
        try {
            return new JSONElement(new JSONObject(IOUtils.toString(new FileInputStream(file), "UTF-8")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private JSONElement parseYamlFile(File file) {
        try {
            Object yamlData = new Yaml().load( new FileInputStream(file));

            if(yamlData instanceof Map) {
                return new JSONElement(new JSONObject((Map<String, Object>) yamlData));
            }
            else if(yamlData instanceof List) {
                return new JSONElement(new JSONArray((ArrayList<Object>) yamlData));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
