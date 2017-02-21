package com.eden.orchid.impl.compilers.parsers;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.compilers.OrchidParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YamlParser extends OrchidParser {
    @Override
    public String[] getSourceExtensions() {
        return new String[] {"yaml", "yml"};
    }

    @Override
    public JSONElement parse(String extension, String input) {
        try {
            Object yamlData = new Yaml().load(input);

            if(yamlData instanceof Map) {
                return new JSONElement(new JSONObject((Map<String, Object>) yamlData));
            }
            else if(yamlData instanceof List) {
                return new JSONElement(new JSONArray((ArrayList<Object>) yamlData));
            }
        }
        catch (Exception e) {}

        return null;
    }
}
